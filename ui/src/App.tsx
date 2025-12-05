import React, { useCallback, useEffect, useMemo, useState } from "react";
import { makeToken, makeListToken } from "./token";

type Contract<T> = {
  contractId: string;
  payload: T;
};

const httpBaseUrl = (import.meta as any).env?.VITE_HTTP_BASE_URL || "/api";
const ledgerId = (import.meta as any).env?.VITE_LEDGER_ID || "sandbox";
const applicationId = (import.meta as any).env?.VITE_APPLICATION_ID || "microlend-ui";
const sharedSecret = (import.meta as any).env?.VITE_SHARED_SECRET || "secret";

type LoanRequest = {
  borrower: string;
  lender: string;
  amount: string;
  rate: string;
  dueDate: string;
  collateralDeposited: boolean;
};

type CollateralHold = {
  owner: string;
  holder: string;
  amount: string;
  loanRequestCid: string;
};

type LoanActive = {
  borrower: string;
  lender: string;
  principal: string;
  rate: string;
  dueDate: string;
  outstanding: string;
  status: string;
  collateralAmount: string;
  collateralCid: string;
};

type LoanRepaid = {
  borrower: string;
  lender: string;
  principal: string;
  rate: string;
  paidAmount: string;
  dueDate: string;
  status: string;
  collateralReturned: boolean;
};

type LoanDefaulted = {
  borrower: string;
  lender: string;
  principal: string;
  rate: string;
  dueDate: string;
  collateralSeized: string;
};

const TR_LoanRequest = "MicroLend.Lending:LoanRequest";
const TR_LoanActive = "MicroLend.Lending:LoanActive";
const TR_LoanRepaid = "MicroLend.Lending:LoanRepaid";
const TR_CollateralHold = "MicroLend.Lending:CollateralHold";
const TR_LoanDefaulted = "MicroLend.Lending:LoanDefaulted";

// ==================== HARDCODED DATA ====================
const HARDCODED_PARTIES = [
  "party::borrower-1::1220abc123",
  "party::borrower-2::1220def456",
  "party::lender-1::1220ghi789",
  "party::lender-2::1220jkl012",
];

const HARDCODED_REQUESTS: Contract<LoanRequest>[] = [
  {
    contractId: "00a1b2c3d4e5f6-request-001",
    payload: {
      borrower: "party::borrower-1::1220abc123",
      lender: "party::lender-1::1220ghi789",
      amount: "100.0",
      rate: "0.10",
      dueDate: "2025-12-31",
      collateralDeposited: false,
    },
  },
  {
    contractId: "00a1b2c3d4e5f6-request-002",
    payload: {
      borrower: "party::borrower-2::1220def456",
      lender: "party::lender-1::1220ghi789",
      amount: "250.0",
      rate: "0.15",
      dueDate: "2025-06-30",
      collateralDeposited: true,
    },
  },
  {
    contractId: "00a1b2c3d4e5f6-request-003",
    payload: {
      borrower: "party::borrower-1::1220abc123",
      lender: "party::lender-2::1220jkl012",
      amount: "500.0",
      rate: "0.12",
      dueDate: "2025-09-15",
      collateralDeposited: true,
    },
  },
];

const HARDCODED_COLLATERALS: Contract<CollateralHold>[] = [
  {
    contractId: "00a1b2c3d4e5f6-collateral-002",
    payload: {
      owner: "party::borrower-2::1220def456",
      holder: "party::lender-1::1220ghi789",
      amount: "287.50",
      loanRequestCid: "00a1b2c3d4e5f6-request-002",
    },
  },
  {
    contractId: "00a1b2c3d4e5f6-collateral-003",
    payload: {
      owner: "party::borrower-1::1220abc123",
      holder: "party::lender-2::1220jkl012",
      amount: "560.0",
      loanRequestCid: "00a1b2c3d4e5f6-request-003",
    },
  },
];

const HARDCODED_ACTIVES: Contract<LoanActive>[] = [
  {
    contractId: "00a1b2c3d4e5f6-active-001",
    payload: {
      borrower: "party::borrower-1::1220abc123",
      lender: "party::lender-1::1220ghi789",
      principal: "1000.0",
      rate: "0.10",
      dueDate: "2025-03-01",
      outstanding: "1100.0",
      status: "Approved",
      collateralAmount: "1100.0",
      collateralCid: "00a1b2c3d4e5f6-collateral-active-001",
    },
  },
  {
    contractId: "00a1b2c3d4e5f6-active-002",
    payload: {
      borrower: "party::borrower-2::1220def456",
      lender: "party::lender-2::1220jkl012",
      principal: "500.0",
      rate: "0.15",
      dueDate: "2024-12-01",
      outstanding: "575.0",
      status: "Overdue",
      collateralAmount: "575.0",
      collateralCid: "00a1b2c3d4e5f6-collateral-active-002",
    },
  },
  {
    contractId: "00a1b2c3d4e5f6-active-003",
    payload: {
      borrower: "party::borrower-1::1220abc123",
      lender: "party::lender-2::1220jkl012",
      principal: "2000.0",
      rate: "0.08",
      dueDate: "2025-06-15",
      outstanding: "2160.0",
      status: "Approved",
      collateralAmount: "2160.0",
      collateralCid: "00a1b2c3d4e5f6-collateral-active-003",
    },
  },
];

const HARDCODED_REPAIDS: Contract<LoanRepaid>[] = [
  {
    contractId: "00a1b2c3d4e5f6-repaid-001",
    payload: {
      borrower: "party::borrower-1::1220abc123",
      lender: "party::lender-1::1220ghi789",
      principal: "500.0",
      rate: "0.10",
      paidAmount: "550.0",
      dueDate: "2024-10-01",
      status: "Repaid",
      collateralReturned: true,
    },
  },
  {
    contractId: "00a1b2c3d4e5f6-repaid-002",
    payload: {
      borrower: "party::borrower-2::1220def456",
      lender: "party::lender-1::1220ghi789",
      principal: "1500.0",
      rate: "0.12",
      paidAmount: "1680.0",
      dueDate: "2024-11-15",
      status: "Repaid",
      collateralReturned: true,
    },
  },
];

const HARDCODED_DEFAULTEDS: Contract<LoanDefaulted>[] = [
  {
    contractId: "00a1b2c3d4e5f6-defaulted-001",
    payload: {
      borrower: "party::borrower-2::1220def456",
      lender: "party::lender-2::1220jkl012",
      principal: "300.0",
      rate: "0.20",
      dueDate: "2024-08-01",
      collateralSeized: "360.0",
    },
  },
];

const USE_HARDCODED_DATA = true;
// ==================== END HARDCODED DATA ====================

// ==================== DARK THEME COLORS ====================
const colors = {
  // Backgrounds
  bgPrimary: "#0a0a0f",
  bgSecondary: "#12121a",
  bgTertiary: "#1a1a24",
  bgCard: "#16161f",
  bgCardHover: "#1e1e2a",
  bgInput: "#1a1a24",

  // Borders
  borderPrimary: "#2a2a3a",
  borderSecondary: "#3a3a4a",
  borderAccent: "#6366f1",

  // Text
  textPrimary: "#f1f1f3",
  textSecondary: "#a0a0b0",
  textMuted: "#6a6a7a",

  // Accents
  accent: "#6366f1",
  accentHover: "#818cf8",
  accentGlow: "rgba(99, 102, 241, 0.3)",

  // Status Colors
  success: "#10b981",
  successBg: "rgba(16, 185, 129, 0.15)",
  warning: "#f59e0b",
  warningBg: "rgba(245, 158, 11, 0.15)",
  danger: "#ef4444",
  dangerBg: "rgba(239, 68, 68, 0.15)",
  info: "#06b6d4",
  infoBg: "rgba(6, 182, 212, 0.15)",
};
// ==================== END DARK THEME COLORS ====================

export default function App() {
  const [party, setParty] = useState("");
  const [knownParties, setKnownParties] = useState<string[]>([]);
  const [errorMsg, setErrorMsg] = useState("");
  const [borrowerForm, setBorrowerForm] = useState({ lender: "", amount: "", rate: "0.10", dueDate: "2025-12-31" });
  const [token, setToken] = useState("");
  const [isConnected, setIsConnected] = useState(false);

  const headers = useMemo(() => ({
    "Content-Type": "application/json",
    Authorization: token ? `Bearer ${token}` : ""
  }), [token]);

  const [requests, setRequests] = useState<Contract<LoanRequest>[]>([]);
  const [collaterals, setCollaterals] = useState<Contract<CollateralHold>[]>([]);
  const [actives, setActives] = useState<Contract<LoanActive>[]>([]);
  const [repaids, setRepaids] = useState<Contract<LoanRepaid>[]>([]);
  const [defaulteds, setDefaulteds] = useState<Contract<LoanDefaulted>[]>([]);

  const calculateCollateral = (amount: string, rate: string): string => {
    const amt = parseFloat(amount) || 0;
    const rt = parseFloat(rate) || 0;
    return (amt + amt * rt).toFixed(2);
  };

  const loadParties = useCallback(async () => {
    if (USE_HARDCODED_DATA) {
      setKnownParties(HARDCODED_PARTIES);
      setErrorMsg("");
      return;
    }

    try {
      const t = await makeListToken(ledgerId, applicationId, sharedSecret);
      const res = await fetch(`${httpBaseUrl}/v2/parties`, {
        method: "GET",
        headers: { Authorization: `Bearer ${t}` },
      });

      if (!res.ok) throw new Error(`HTTP ${res.status}`);

      const json = await res.json();
      const ids = (json.partyDetails || []).map((p: any) => p.party as string);
      setKnownParties(ids);
      setErrorMsg("");
    } catch (e: any) {
      console.error(e);
      setKnownParties([]);
      setErrorMsg("Failed to load parties from JSON Ledger API");
    }
  }, []);

  const loadContracts = useCallback(async () => {
    if (USE_HARDCODED_DATA) {
      setRequests(HARDCODED_REQUESTS);
      setCollaterals(HARDCODED_COLLATERALS);
      setActives(HARDCODED_ACTIVES);
      setRepaids(HARDCODED_REPAIDS);
      setDefaulteds(HARDCODED_DEFAULTEDS);
      setErrorMsg("");
      return;
    }

    if (!token) return;

    try {
      const endRes = await fetch(`${httpBaseUrl}/v2/state/ledger-end`, {
        method: "GET",
        headers,
      });
      if (!endRes.ok) throw new Error(`ledger-end HTTP ${endRes.status}`);
      const { offset } = await endRes.json();

      const body = {
        filter: {
          filtersByParty: {},
          filtersForAnyParty: {
            cumulative: [
              {
                identifierFilter: {
                  WildcardFilter: {
                    value: { includeCreatedEventBlob: true },
                  },
                },
              },
            ],
          },
        },
        verbose: false,
        activeAtOffset: offset,
        eventFormat: null,
      };

      const res = await fetch(`${httpBaseUrl}/v2/state/active-contracts`, {
        method: "POST",
        headers,
        body: JSON.stringify(body),
      });

      if (!res.ok) throw new Error(`active-contracts HTTP ${res.status}`);

      const json = await res.json();
      const activeContracts = json.activeContracts || [];

      type Raw = {
        createdEvent: {
          contractId: string;
          templateId: string;
          createArgument: any;
        };
      };

      const all = (activeContracts as Raw[]).map((ev) => ({
        contractId: ev.createdEvent.contractId,
        templateId: ev.createdEvent.templateId,
        payload: ev.createdEvent.createArgument,
      }));

      setRequests(all.filter((c) => c.templateId.endsWith(":MicroLend.Lending:LoanRequest")) as any);
      setCollaterals(all.filter((c) => c.templateId.endsWith(":MicroLend.Lending:CollateralHold")) as any);
      setActives(all.filter((c) => c.templateId.endsWith(":MicroLend.Lending:LoanActive")) as any);
      setRepaids(all.filter((c) => c.templateId.endsWith(":MicroLend.Lending:LoanRepaid")) as any);
      setDefaulteds(all.filter((c) => c.templateId.endsWith(":MicroLend.Lending:LoanDefaulted")) as any);
      setErrorMsg("");
    } catch (e: any) {
      console.error(e);
      setErrorMsg(e.message || "Failed to load contracts");
    }
  }, [headers, token]);

  useEffect(() => {
    loadParties();
  }, [loadParties]);

  useEffect(() => {
    if (isConnected) {
      loadContracts();
    }
  }, [isConnected, loadContracts]);

  const handleConnect = async () => {
    if (USE_HARDCODED_DATA) {
      setToken("hardcoded-token");
      setIsConnected(true);
    } else {
      const newToken = await makeToken(party, ledgerId, applicationId, sharedSecret);
      setToken(newToken);
      setIsConnected(true);
    }
  };

  const handleDisconnect = () => {
    setToken("");
    setIsConnected(false);
    setParty("");
    setRequests([]);
    setCollaterals([]);
    setActives([]);
    setRepaids([]);
    setDefaulteds([]);
  };

  const createRequest = useCallback(async () => {
    if (USE_HARDCODED_DATA) {
      const newRequest: Contract<LoanRequest> = {
        contractId: `00a1b2c3d4e5f6-request-${Date.now()}`,
        payload: {
          borrower: party,
          lender: borrowerForm.lender || party,
          amount: borrowerForm.amount,
          rate: borrowerForm.rate,
          dueDate: borrowerForm.dueDate,
          collateralDeposited: false,
        },
      };
      setRequests([...requests, newRequest]);
      setBorrowerForm({ lender: "", amount: "", rate: "0.10", dueDate: "2025-12-31" });
      return;
    }

    if (!token) return;

    const payload: LoanRequest = {
      borrower: party,
      lender: borrowerForm.lender || party,
      amount: borrowerForm.amount,
      rate: borrowerForm.rate,
      dueDate: borrowerForm.dueDate,
      collateralDeposited: false,
    };

    const body = {
      commandId: crypto.randomUUID(),
      applicationId,
      actAs: [party],
      readAs: [],
      commands: [
        {
          CreateCommand: {
            templateId: TR_LoanRequest,
            createArguments: payload,
          },
        },
      ],
    };

    try {
      const res = await fetch(
        `${httpBaseUrl}/v2/commands/submit-and-wait-for-transaction-tree`,
        { method: "POST", headers, body: JSON.stringify(body) }
      );
      if (!res.ok) throw new Error(`Create failed (${res.status})`);
      await loadContracts();
    } catch (e: any) {
      setErrorMsg(e.message || "Failed to create LoanRequest");
    }
  }, [headers, party, borrowerForm, token, loadContracts, requests]);

  const depositCollateral = useCallback(async (cid: string) => {
    if (USE_HARDCODED_DATA) {
      const updatedRequests = requests.map(r => {
        if (r.contractId === cid) {
          return { ...r, payload: { ...r.payload, collateralDeposited: true } };
        }
        return r;
      });

      const request = requests.find(r => r.contractId === cid);
      if (request) {
        const collateralAmount = calculateCollateral(request.payload.amount, request.payload.rate);
        const newCollateral: Contract<CollateralHold> = {
          contractId: `00a1b2c3d4e5f6-collateral-${Date.now()}`,
          payload: {
            owner: request.payload.borrower,
            holder: request.payload.lender,
            amount: collateralAmount,
            loanRequestCid: cid,
          },
        };
        setCollaterals([...collaterals, newCollateral]);
      }

      setRequests(updatedRequests);
      return;
    }

    const body = {
      commandId: crypto.randomUUID(),
      applicationId,
      actAs: [party],
      readAs: [],
      commands: [
        {
          ExerciseCommand: {
            templateId: TR_LoanRequest,
            contractId: cid,
            choice: "DepositCollateral",
            choiceArgument: {},
          },
        },
      ],
    };

    try {
      const res = await fetch(
        `${httpBaseUrl}/v2/commands/submit-and-wait-for-transaction-tree`,
        { method: "POST", headers, body: JSON.stringify(body) }
      );
      if (!res.ok) throw new Error(`DepositCollateral failed (${res.status})`);
      await loadContracts();
    } catch (e: any) {
      setErrorMsg(e.message || "Failed to deposit collateral");
    }
  }, [headers, party, loadContracts, requests, collaterals]);

  const approve = useCallback(async (cid: string, collateralCid: string) => {
    if (USE_HARDCODED_DATA) {
      const request = requests.find(r => r.contractId === cid);
      if (request) {
        const collateralAmount = calculateCollateral(request.payload.amount, request.payload.rate);
        const newActive: Contract<LoanActive> = {
          contractId: `00a1b2c3d4e5f6-active-${Date.now()}`,
          payload: {
            borrower: request.payload.borrower,
            lender: request.payload.lender,
            principal: request.payload.amount,
            rate: request.payload.rate,
            dueDate: request.payload.dueDate,
            outstanding: collateralAmount,
            status: "Approved",
            collateralAmount: collateralAmount,
            collateralCid: collateralCid,
          },
        };
        setActives([...actives, newActive]);
        setRequests(requests.filter(r => r.contractId !== cid));
        setCollaterals(collaterals.filter(c => c.contractId !== collateralCid));
      }
      return;
    }

    const body = {
      commandId: crypto.randomUUID(),
      applicationId,
      actAs: [party],
      readAs: [],
      commands: [
        {
          ExerciseCommand: {
            templateId: TR_LoanRequest,
            contractId: cid,
            choice: "Approve",
            choiceArgument: { collateralCid },
          },
        },
      ],
    };

    try {
      const res = await fetch(
        `${httpBaseUrl}/v2/commands/submit-and-wait-for-transaction-tree`,
        { method: "POST", headers, body: JSON.stringify(body) }
      );
      if (!res.ok) throw new Error(`Approve failed (${res.status})`);
      await loadContracts();
    } catch (e: any) {
      setErrorMsg(e.message || "Failed to approve loan");
    }
  }, [headers, party, loadContracts, requests, actives, collaterals]);

  const reject = useCallback(async (cid: string) => {
    if (USE_HARDCODED_DATA) {
      setRequests(requests.filter(r => r.contractId !== cid));
      setCollaterals(collaterals.filter(c => c.payload.loanRequestCid !== cid));
      return;
    }

    const body = {
      commandId: crypto.randomUUID(),
      applicationId,
      actAs: [party],
      readAs: [],
      commands: [
        {
          ExerciseCommand: {
            templateId: TR_LoanRequest,
            contractId: cid,
            choice: "Reject",
            choiceArgument: {},
          },
        },
      ],
    };

    try {
      const res = await fetch(
        `${httpBaseUrl}/v2/commands/submit-and-wait-for-transaction-tree`,
        { method: "POST", headers, body: JSON.stringify(body) }
      );
      if (!res.ok) throw new Error(`Reject failed (${res.status})`);
      await loadContracts();
    } catch (e: any) {
      setErrorMsg(e.message || "Failed to reject loan");
    }
  }, [headers, party, loadContracts, requests, collaterals]);

  const repay = useCallback(async (cid: string, amount: string) => {
    if (USE_HARDCODED_DATA) {
      const active = actives.find(a => a.contractId === cid);
      if (active) {
        const newRepaid: Contract<LoanRepaid> = {
          contractId: `00a1b2c3d4e5f6-repaid-${Date.now()}`,
          payload: {
            borrower: active.payload.borrower,
            lender: active.payload.lender,
            principal: active.payload.principal,
            rate: active.payload.rate,
            paidAmount: amount,
            dueDate: active.payload.dueDate,
            status: "Repaid",
            collateralReturned: true,
          },
        };
        setRepaids([...repaids, newRepaid]);
        setActives(actives.filter(a => a.contractId !== cid));
      }
      return;
    }

    const body = {
      commandId: crypto.randomUUID(),
      applicationId,
      actAs: [party],
      readAs: [],
      commands: [
        {
          ExerciseCommand: {
            templateId: TR_LoanActive,
            contractId: cid,
            choice: "Repay",
            choiceArgument: { amount },
          },
        },
      ],
    };

    try {
      const res = await fetch(
        `${httpBaseUrl}/v2/commands/submit-and-wait-for-transaction-tree`,
        { method: "POST", headers, body: JSON.stringify(body) }
      );
      if (!res.ok) throw new Error(`Repay failed (${res.status})`);
      await loadContracts();
    } catch (e: any) {
      setErrorMsg(e.message || "Failed to repay loan");
    }
  }, [headers, party, loadContracts, actives, repaids]);

  const markOverdue = useCallback(async (cid: string, currentDate: string) => {
    if (USE_HARDCODED_DATA) {
      const updatedActives = actives.map(a => {
        if (a.contractId === cid) {
          return { ...a, payload: { ...a.payload, status: "Overdue" } };
        }
        return a;
      });
      setActives(updatedActives);
      return;
    }

    const body = {
      commandId: crypto.randomUUID(),
      applicationId,
      actAs: [party],
      readAs: [],
      commands: [
        {
          ExerciseCommand: {
            templateId: TR_LoanActive,
            contractId: cid,
            choice: "MarkOverdue",
            choiceArgument: { currentDate },
          },
        },
      ],
    };

    try {
      const res = await fetch(
        `${httpBaseUrl}/v2/commands/submit-and-wait-for-transaction-tree`,
        { method: "POST", headers, body: JSON.stringify(body) }
      );
      if (!res.ok) throw new Error(`MarkOverdue failed (${res.status})`);
      await loadContracts();
    } catch (e: any) {
      setErrorMsg(e.message || "Failed to mark overdue");
    }
  }, [headers, party, loadContracts, actives]);

  const seizeCollateral = useCallback(async (cid: string) => {
    if (USE_HARDCODED_DATA) {
      const active = actives.find(a => a.contractId === cid);
      if (active) {
        const newDefaulted: Contract<LoanDefaulted> = {
          contractId: `00a1b2c3d4e5f6-defaulted-${Date.now()}`,
          payload: {
            borrower: active.payload.borrower,
            lender: active.payload.lender,
            principal: active.payload.principal,
            rate: active.payload.rate,
            dueDate: active.payload.dueDate,
            collateralSeized: active.payload.collateralAmount,
          },
        };
        setDefaulteds([...defaulteds, newDefaulted]);
        setActives(actives.filter(a => a.contractId !== cid));
      }
      return;
    }

    const body = {
      commandId: crypto.randomUUID(),
      applicationId,
      actAs: [party],
      readAs: [],
      commands: [
        {
          ExerciseCommand: {
            templateId: TR_LoanActive,
            contractId: cid,
            choice: "SeizeCollateralFromLoan",
            choiceArgument: {},
          },
        },
      ],
    };

    try {
      const res = await fetch(
        `${httpBaseUrl}/v2/commands/submit-and-wait-for-transaction-tree`,
        { method: "POST", headers, body: JSON.stringify(body) }
      );
      if (!res.ok) throw new Error(`SeizeCollateral failed (${res.status})`);
      await loadContracts();
    } catch (e: any) {
      setErrorMsg(e.message || "Failed to seize collateral");
    }
  }, [headers, party, loadContracts, actives, defaulteds]);

  const truncateId = (id: string) => {
    if (id.length > 20) return `${id.slice(0, 10)}...${id.slice(-8)}`;
    return id;
  };

  const findCollateralForRequest = (requestCid: string): Contract<CollateralHold> | undefined => {
    return collaterals.find(c => c.payload.loanRequestCid === requestCid);
  };

  // ==================== RENDER ====================
  return (
    <div style={styles.container}>
      {/* Header */}
      <header style={styles.header}>
        <div style={styles.headerContent}>
          <div style={styles.logoContainer}>
            <div style={styles.logoIcon}>◈</div>
            <h1 style={styles.logo}>MicroLend</h1>
          </div>
        </div>
        {isConnected && (
          <div style={styles.headerRight}>
            <div style={styles.connectedBadge}>
              <span style={styles.connectedDot}></span>
              <span>{truncateId(party)}</span>
            </div>
            <button onClick={handleDisconnect} style={styles.disconnectBtn}>
              Disconnect
            </button>
          </div>
        )}
      </header>

      {/* Error Message */}
      {errorMsg && (
        <div style={styles.errorBox}>
          <span>⚠</span> {errorMsg}
          <button onClick={() => setErrorMsg("")} style={styles.errorClose}>×</button>
        </div>
      )}

      {/* Main Content */}
      {!isConnected ? (
        // ==================== NOT CONNECTED VIEW ====================
        <div style={styles.connectContainer}>
          <div style={styles.connectCard}>
            <div style={styles.connectIconWrapper}>
              <div style={styles.connectIconInner}>🔐</div>
            </div>
            <h2 style={styles.connectTitle}>Connect Your Wallet</h2>
            <p style={styles.connectSubtitle}>
              Select your party to start using MicroLend
            </p>
            <div style={styles.connectForm}>
              <select
                value={party}
                onChange={e => setParty(e.target.value)}
                style={styles.connectSelect}
              >
                <option value="">Select your party...</option>
                {knownParties.map(id => (
                  <option key={id} value={id}>{truncateId(id)}</option>
                ))}
              </select>
              <button
                onClick={handleConnect}
                disabled={!party}
                style={{
                  ...styles.connectButton,
                  opacity: !party ? 0.5 : 1,
                  cursor: !party ? "not-allowed" : "pointer"
                }}
              >
                Connect Wallet
              </button>
            </div>
            <button onClick={loadParties} style={styles.refreshLink}>
              ↻ Refresh Parties
            </button>
          </div>

          {/* Features Section */}
          <div style={styles.featuresGrid}>
            <div style={styles.featureCard}>
              <div style={styles.featureIconWrapper}>💰</div>
              <h3 style={styles.featureTitle}>Borrow</h3>
              <p style={styles.featureDesc}>Request loans with collateral backing</p>
            </div>
            <div style={styles.featureCard}>
              <div style={styles.featureIconWrapper}>🏦</div>
              <h3 style={styles.featureTitle}>Lend</h3>
              <p style={styles.featureDesc}>Approve loans and earn interest</p>
            </div>
            <div style={styles.featureCard}>
              <div style={styles.featureIconWrapper}>🔒</div>
              <h3 style={styles.featureTitle}>Secure</h3>
              <p style={styles.featureDesc}>Collateral-backed smart contracts</p>
            </div>
          </div>
        </div>
      ) : (
        // ==================== CONNECTED VIEW ====================
        <div style={styles.mainContent}>
          {/* Stats Row */}
          <div style={styles.statsRow}>
            <div style={styles.statCard}>
              <div style={{ ...styles.statNumber, color: colors.accent }}>{requests.length}</div>
              <div style={styles.statLabel}>Pending</div>
            </div>
            <div style={styles.statCard}>
              <div style={{ ...styles.statNumber, color: colors.info }}>{actives.length}</div>
              <div style={styles.statLabel}>Active</div>
            </div>
            <div style={styles.statCard}>
              <div style={{ ...styles.statNumber, color: colors.success }}>{repaids.length}</div>
              <div style={styles.statLabel}>Completed</div>
            </div>
            <div style={styles.statCard}>
              <div style={{ ...styles.statNumber, color: colors.danger }}>{defaulteds.length}</div>
              <div style={styles.statLabel}>Defaulted</div>
            </div>
          </div>

          {/* Two Column Layout */}
          <div style={styles.twoColumnLayout}>
            {/* Left Column - Create Form */}
            <div style={styles.leftColumn}>
              <div style={styles.card}>
                <div style={styles.cardHeader}>
                  <h2 style={styles.cardTitle}>Create Loan Request</h2>
                </div>

                {borrowerForm.amount && (
                  <div style={styles.collateralPreview}>
                    <span>🔒 Required Collateral:</span>
                    <strong>${calculateCollateral(borrowerForm.amount, borrowerForm.rate)}</strong>
                  </div>
                )}

                <div style={styles.formGroup}>
                  <label style={styles.label}>Lender</label>
                  <select
                    value={borrowerForm.lender}
                    onChange={e => setBorrowerForm({ ...borrowerForm, lender: e.target.value })}
                    style={styles.input}
                  >
                    <option value="">Select lender...</option>
                    {knownParties.filter(p => p !== party).map(id => (
                      <option key={id} value={id}>{truncateId(id)}</option>
                    ))}
                  </select>
                </div>

                <div style={styles.formRow}>
                  <div style={styles.formGroup}>
                    <label style={styles.label}>Amount</label>
                    <input
                      value={borrowerForm.amount}
                      onChange={e => setBorrowerForm({ ...borrowerForm, amount: e.target.value })}
                      style={styles.input}
                      placeholder="0.00"
                      type="number"
                    />
                  </div>
                  <div style={styles.formGroup}>
                    <label style={styles.label}>Rate</label>
                    <input
                      value={borrowerForm.rate}
                      onChange={e => setBorrowerForm({ ...borrowerForm, rate: e.target.value })}
                      style={styles.input}
                      placeholder="0.10"
                    />
                  </div>
                </div>

                <div style={styles.formGroup}>
                  <label style={styles.label}>Due Date</label>
                  <input
                    value={borrowerForm.dueDate}
                    onChange={e => setBorrowerForm({ ...borrowerForm, dueDate: e.target.value })}
                    style={styles.input}
                    type="date"
                  />
                </div>

                <button
                  onClick={createRequest}
                  disabled={!borrowerForm.amount || !borrowerForm.lender}
                  style={{
                    ...styles.primaryButton,
                    opacity: (!borrowerForm.amount || !borrowerForm.lender) ? 0.5 : 1,
                  }}
                >
                  Create Request
                </button>
              </div>
            </div>

            {/* Right Column - Loan Lists */}
            <div style={styles.rightColumn}>
              {/* Loan Requests */}
              <div style={styles.card}>
                <div style={styles.cardHeader}>
                  <h2 style={styles.cardTitle}>Loan Requests</h2>
                  <span style={styles.badge}>{requests.length}</span>
                </div>

                {requests.length === 0 ? (
                  <div style={styles.emptyState}>
                    <span style={styles.emptyIcon}>📭</span>
                    <p>No pending requests</p>
                  </div>
                ) : (
                  <div style={styles.loanList}>
                    {requests.map(c => {
                      const collateral = findCollateralForRequest(c.contractId);
                      const collateralRequired = calculateCollateral(c.payload.amount, c.payload.rate);

                      return (
                        <div key={c.contractId} style={{
                          ...styles.loanItem,
                          borderLeftColor: c.payload.collateralDeposited ? colors.info : colors.warning
                        }}>
                          <div style={styles.loanItemHeader}>
                            <span style={styles.loanAmount}>${c.payload.amount}</span>
                            <span style={{
                              ...styles.statusBadge,
                              background: c.payload.collateralDeposited ? colors.infoBg : colors.warningBg,
                              color: c.payload.collateralDeposited ? colors.info : colors.warning
                            }}>
                              {c.payload.collateralDeposited ? "Ready" : "Pending Collateral"}
                            </span>
                          </div>
                          <div style={styles.loanDetails}>
                            <span>Rate: {(parseFloat(c.payload.rate) * 100).toFixed(1)}%</span>
                            <span>Due: {c.payload.dueDate}</span>
                            <span>Collateral: ${collateralRequired}</span>
                          </div>
                          <div style={styles.loanParties}>
                            <small>From: {truncateId(c.payload.borrower)}</small>
                            <small>To: {truncateId(c.payload.lender)}</small>
                          </div>
                          <div style={styles.loanActions}>
                            {!c.payload.collateralDeposited && (
                              <button onClick={() => depositCollateral(c.contractId)} style={styles.warningBtn}>
                                Deposit ${collateralRequired}
                              </button>
                            )}
                            {c.payload.collateralDeposited && collateral && (
                              <button onClick={() => approve(c.contractId, collateral.contractId)} style={styles.successBtn}>
                                Approve
                              </button>
                            )}
                            <button onClick={() => reject(c.contractId)} style={styles.dangerBtn}>
                              Reject
                            </button>
                          </div>
                        </div>
                      );
                    })}
                  </div>
                )}
              </div>

              {/* Active Loans */}
              <div style={styles.card}>
                <div style={styles.cardHeader}>
                  <h2 style={styles.cardTitle}>Active Loans</h2>
                  <span style={{ ...styles.badge, background: colors.infoBg, color: colors.info }}>{actives.length}</span>
                </div>

                {actives.length === 0 ? (
                  <div style={styles.emptyState}>
                    <span style={styles.emptyIcon}>💤</span>
                    <p>No active loans</p>
                  </div>
                ) : (
                  <div style={styles.loanList}>
                    {actives.map(c => (
                      <div key={c.contractId} style={{
                        ...styles.loanItem,
                        borderLeftColor: c.payload.status === "Overdue" ? colors.danger : colors.info
                      }}>
                        <div style={styles.loanItemHeader}>
                          <span style={styles.loanAmount}>${c.payload.outstanding}</span>
                          <span style={{
                            ...styles.statusBadge,
                            background: c.payload.status === "Overdue" ? colors.dangerBg : colors.infoBg,
                            color: c.payload.status === "Overdue" ? colors.danger : colors.info
                          }}>
                            {c.payload.status}
                          </span>
                        </div>
                        <div style={styles.loanDetails}>
                          <span>Principal: ${c.payload.principal}</span>
                          <span>Due: {c.payload.dueDate}</span>
                          <span>Collateral: ${c.payload.collateralAmount}</span>
                        </div>
                        <div style={styles.loanParties}>
                          <small>Borrower: {truncateId(c.payload.borrower)}</small>
                          <small>Lender: {truncateId(c.payload.lender)}</small>
                        </div>
                        <div style={styles.loanActions}>
                          <button onClick={() => repay(c.contractId, c.payload.outstanding)} style={styles.successBtn}>
                            Repay
                          </button>
                          {c.payload.status !== "Overdue" && (
                            <button onClick={() => markOverdue(c.contractId, new Date().toISOString().slice(0, 10))} style={styles.warningBtn}>
                              Mark Overdue
                            </button>
                          )}
                          {c.payload.status === "Overdue" && (
                            <button onClick={() => seizeCollateral(c.contractId)} style={styles.dangerBtn}>
                              Seize Collateral
                            </button>
                          )}
                        </div>
                      </div>
                    ))}
                  </div>
                )}
              </div>

              {/* Completed & Defaulted */}
              <div style={styles.twoColumnSmall}>
                {/* Completed Loans */}
                <div style={styles.card}>
                  <div style={styles.cardHeader}>
                    <h2 style={styles.cardTitle}>Completed</h2>
                    <span style={{ ...styles.badge, background: colors.successBg, color: colors.success }}>{repaids.length}</span>
                  </div>

                  {repaids.length === 0 ? (
                    <div style={styles.emptyStateSmall}>
                      <span>🎯</span>
                      <p>No completed loans</p>
                    </div>
                  ) : (
                    <div style={styles.loanListCompact}>
                      {repaids.map(c => (
                        <div key={c.contractId} style={{ ...styles.loanItemCompact, borderLeftColor: colors.success }}>
                          <div style={styles.compactHeader}>
                            <span style={styles.compactAmount}>${c.payload.paidAmount}</span>
                            <span style={{ ...styles.compactBadge, background: colors.successBg, color: colors.success }}>
                              Repaid
                            </span>
                          </div>
                          <small style={styles.compactDetail}>
                            Principal: ${c.payload.principal}
                          </small>
                        </div>
                      ))}
                    </div>
                  )}
                </div>

                {/* Defaulted Loans */}
                <div style={styles.card}>
                  <div style={styles.cardHeader}>
                    <h2 style={styles.cardTitle}>Defaulted</h2>
                    <span style={{ ...styles.badge, background: colors.dangerBg, color: colors.danger }}>{defaulteds.length}</span>
                  </div>

                  {defaulteds.length === 0 ? (
                    <div style={styles.emptyStateSmall}>
                      <span>🛡️</span>
                      <p>No defaults</p>
                    </div>
                  ) : (
                    <div style={styles.loanListCompact}>
                      {defaulteds.map(c => (
                        <div key={c.contractId} style={{ ...styles.loanItemCompact, borderLeftColor: colors.danger }}>
                          <div style={styles.compactHeader}>
                            <span style={styles.compactAmount}>${c.payload.principal}</span>
                            <span style={{ ...styles.compactBadge, background: colors.dangerBg, color: colors.danger }}>
                              Seized: ${c.payload.collateralSeized}
                            </span>
                          </div>
                          <small style={styles.compactDetail}>
                            Due: {c.payload.dueDate}
                          </small>
                        </div>
                      ))}
                    </div>
                  )}
                </div>
              </div>
            </div>
          </div>
        </div>
      )}

      {/* Footer */}
      <footer style={styles.footer}>
        <p>Kucing Oyen</p>
      </footer>
    </div>
  );
}

// ==================== DARK THEME STYLES ====================
const styles: { [key: string]: React.CSSProperties } = {
  container: {
    fontFamily: "'Inter', 'Segoe UI', 'Roboto', sans-serif",
    minHeight: "100vh",
    background: colors.bgPrimary,
    display: "flex",
    flexDirection: "column",
    color: colors.textPrimary,
  },
  header: {
    display: "flex",
    justifyContent: "space-between",
    alignItems: "center",
    padding: "16px 32px",
    background: colors.bgSecondary,
    borderBottom: `1px solid ${colors.borderPrimary}`,
  },
  headerContent: {
    display: "flex",
    alignItems: "center",
    gap: 16,
  },
  logoContainer: {
    display: "flex",
    alignItems: "center",
    gap: 12,
  },
  logoIcon: {
    fontSize: 28,
    color: colors.accent,
    fontWeight: 700,
  },
  logo: {
    fontSize: 24,
    fontWeight: 700,
    color: colors.textPrimary,
    margin: 0,
    letterSpacing: "-0.5px",
  },
  headerRight: {
    display: "flex",
    alignItems: "center",
    gap: 12,
  },
  connectedBadge: {
    display: "flex",
    alignItems: "center",
    gap: 8,
    background: colors.bgTertiary,
    padding: "8px 16px",
    borderRadius: 8,
    color: colors.textPrimary,
    fontSize: 13,
    border: `1px solid ${colors.borderPrimary}`,
  },
  connectedDot: {
    width: 8,
    height: 8,
    borderRadius: "50%",
    background: colors.success,
    boxShadow: `0 0 8px ${colors.success}`,
  },
  disconnectBtn: {
    background: colors.bgTertiary,
    color: colors.textSecondary,
    border: `1px solid ${colors.borderPrimary}`,
    padding: "8px 16px",
    borderRadius: 8,
    cursor: "pointer",
    fontSize: 13,
    transition: "all 0.2s",
  },
  errorBox: {
    background: colors.dangerBg,
    color: colors.danger,
    padding: "12px 24px",
    display: "flex",
    alignItems: "center",
    gap: 8,
    fontSize: 14,
    borderBottom: `1px solid ${colors.danger}`,
  },
  errorClose: {
    marginLeft: "auto",
    background: "none",
    border: "none",
    color: colors.danger,
    fontSize: 20,
    cursor: "pointer",
  },

  // Connect View
  connectContainer: {
    flex: 1,
    display: "flex",
    flexDirection: "column",
    alignItems: "center",
    justifyContent: "center",
    padding: "40px 20px",
  },
  connectCard: {
    background: colors.bgCard,
    borderRadius: 16,
    padding: "48px",
    textAlign: "center",
    boxShadow: `0 20px 60px rgba(0,0,0,0.5)`,
    border: `1px solid ${colors.borderPrimary}`,
    maxWidth: 420,
    width: "100%",
  },
  connectIconWrapper: {
    width: 80,
    height: 80,
    borderRadius: "50%",
    background: colors.accentGlow,
    display: "flex",
    alignItems: "center",
    justifyContent: "center",
    margin: "0 auto 24px",
    border: `2px solid ${colors.accent}`,
  },
  connectIconInner: {
    fontSize: 36,
  },
  connectTitle: {
    fontSize: 24,
    fontWeight: 600,
    color: colors.textPrimary,
    margin: "0 0 8px 0",
  },
  connectSubtitle: {
    color: colors.textSecondary,
    fontSize: 14,
    margin: "0 0 32px 0",
  },
  connectForm: {
    display: "flex",
    flexDirection: "column",
    gap: 16,
  },
  connectSelect: {
    padding: "14px 16px",
    borderRadius: 10,
    border: `1px solid ${colors.borderSecondary}`,
    fontSize: 14,
    outline: "none",
    background: colors.bgInput,
    color: colors.textPrimary,
    cursor: "pointer",
  },
  connectButton: {
    background: colors.accent,
    color: "#fff",
    border: "none",
    padding: "14px",
    borderRadius: 10,
    fontSize: 15,
    fontWeight: 600,
    cursor: "pointer",
    transition: "all 0.2s",
  },
  refreshLink: {
    background: "none",
    border: "none",
    color: colors.textMuted,
    marginTop: 20,
    cursor: "pointer",
    fontSize: 13,
    transition: "color 0.2s",
  },
  featuresGrid: {
    display: "grid",
    gridTemplateColumns: "repeat(3, 1fr)",
    gap: 20,
    marginTop: 48,
    maxWidth: 700,
    width: "100%",
  },
  featureCard: {
    background: colors.bgCard,
    border: `1px solid ${colors.borderPrimary}`,
    borderRadius: 12,
    padding: "28px 20px",
    textAlign: "center",
  },
  featureIconWrapper: {
    fontSize: 32,
    marginBottom: 12,
  },
  featureTitle: {
    fontSize: 16,
    fontWeight: 600,
    margin: "0 0 8px 0",
    color: colors.textPrimary,
  },
  featureDesc: {
    fontSize: 13,
    color: colors.textMuted,
    margin: 0,
    lineHeight: 1.5,
  },

  // Main Content
  mainContent: {
    flex: 1,
    padding: "24px 32px",
  },
  statsRow: {
    display: "grid",
    gridTemplateColumns: "repeat(4, 1fr)",
    gap: 16,
    marginBottom: 24,
  },
  statCard: {
    background: colors.bgCard,
    borderRadius: 12,
    padding: "24px 20px",
    textAlign: "center",
    border: `1px solid ${colors.borderPrimary}`,
  },
  statNumber: {
    fontSize: 36,
    fontWeight: 700,
  },
  statLabel: {
    fontSize: 13,
    color: colors.textMuted,
    marginTop: 4,
    textTransform: "uppercase",
    letterSpacing: "0.5px",
  },
  twoColumnLayout: {
    display: "grid",
    gridTemplateColumns: "360px 1fr",
    gap: 24,
  },
  leftColumn: {},
  rightColumn: {
    display: "flex",
    flexDirection: "column",
    gap: 24,
  },
  card: {
    background: colors.bgCard,
    borderRadius: 12,
    padding: 24,
    border: `1px solid ${colors.borderPrimary}`,
  },
  cardHeader: {
    display: "flex",
    alignItems: "center",
    gap: 10,
    marginBottom: 20,
    paddingBottom: 16,
    borderBottom: `1px solid ${colors.borderPrimary}`,
  },
  cardTitle: {
    fontSize: 16,
    fontWeight: 600,
    color: colors.textPrimary,
    margin: 0,
    flex: 1,
  },
  badge: {
    background: colors.accentGlow,
    color: colors.accent,
    padding: "4px 12px",
    borderRadius: 6,
    fontSize: 13,
    fontWeight: 600,
  },
  formGroup: {
    marginBottom: 16,
  },
  formRow: {
    display: "grid",
    gridTemplateColumns: "1fr 1fr",
    gap: 12,
  },
  label: {
    display: "block",
    fontSize: 12,
    fontWeight: 500,
    color: colors.textSecondary,
    marginBottom: 8,
    textTransform: "uppercase",
    letterSpacing: "0.5px",
  },
  input: {
    width: "100%",
    padding: "12px 14px",
    borderRadius: 8,
    border: `1px solid ${colors.borderSecondary}`,
    fontSize: 14,
    outline: "none",
    boxSizing: "border-box",
    background: colors.bgInput,
    color: colors.textPrimary,
    transition: "border-color 0.2s",
  },
  collateralPreview: {
    background: colors.warningBg,
    border: `1px solid ${colors.warning}`,
    borderRadius: 8,
    padding: "12px 14px",
    marginBottom: 16,
    fontSize: 13,
    display: "flex",
    justifyContent: "space-between",
    color: colors.warning,
  },
  primaryButton: {
    width: "100%",
    background: colors.accent,
    color: "#fff",
    border: "none",
    padding: "14px",
    borderRadius: 8,
    fontSize: 14,
    fontWeight: 600,
    cursor: "pointer",
    marginTop: 8,
    transition: "all 0.2s",
  },
  emptyState: {
    textAlign: "center",
    padding: "40px 20px",
    color: colors.textMuted,
  },
  emptyIcon: {
    fontSize: 40,
    display: "block",
    marginBottom: 12,
    opacity: 0.5,
  },
  emptyStateSmall: {
    textAlign: "center",
    padding: "24px",
    color: colors.textMuted,
    fontSize: 13,
  },
  loanList: {
    display: "flex",
    flexDirection: "column",
    gap: 12,
    maxHeight: 400,
    overflowY: "auto",
  },
  loanItem: {
    background: colors.bgTertiary,
    borderRadius: 10,
    padding: 16,
    borderLeft: "4px solid",
    borderLeftColor: colors.accent,
  },
  loanItemHeader: {
    display: "flex",
    justifyContent: "space-between",
    alignItems: "center",
    marginBottom: 10,
  },
  loanAmount: {
    fontSize: 22,
    fontWeight: 700,
    color: colors.textPrimary,
  },
  statusBadge: {
    padding: "4px 10px",
    borderRadius: 6,
    fontSize: 11,
    fontWeight: 600,
    textTransform: "uppercase",
    letterSpacing: "0.3px",
  },
  loanDetails: {
    display: "flex",
    gap: 16,
    fontSize: 12,
    color: colors.textSecondary,
    marginBottom: 10,
  },
  loanParties: {
    display: "flex",
    flexDirection: "column",
    gap: 4,
    fontSize: 11,
    color: colors.textMuted,
    marginBottom: 14,
  },
  loanActions: {
    display: "flex",
    gap: 8,
    flexWrap: "wrap",
  },
  successBtn: {
    background: colors.success,
    color: "#fff",
    border: "none",
    padding: "8px 16px",
    borderRadius: 6,
    fontSize: 12,
    fontWeight: 600,
    cursor: "pointer",
    transition: "all 0.2s",
  },
  warningBtn: {
    background: colors.warning,
    color: "#000",
    border: "none",
    padding: "8px 16px",
    borderRadius: 6,
    fontSize: 12,
    fontWeight: 600,
    cursor: "pointer",
    transition: "all 0.2s",
  },
  dangerBtn: {
    background: colors.danger,
    color: "#fff",
    border: "none",
    padding: "8px 16px",
    borderRadius: 6,
    fontSize: 12,
    fontWeight: 600,
    cursor: "pointer",
    transition: "all 0.2s",
  },
  twoColumnSmall: {
    display: "grid",
    gridTemplateColumns: "1fr 1fr",
    gap: 24,
  },
  loanListCompact: {
    display: "flex",
    flexDirection: "column",
    gap: 8,
    maxHeight: 250,
    overflowY: "auto",
  },
  loanItemCompact: {
    background: colors.bgTertiary,
    borderRadius: 8,
    padding: 12,
    borderLeft: "3px solid",
  },
  compactHeader: {
    display: "flex",
    justifyContent: "space-between",
    alignItems: "center",
    marginBottom: 6,
  },
  compactAmount: {
    fontSize: 16,
    fontWeight: 600,
    color: colors.textPrimary,
  },
  compactBadge: {
    padding: "3px 8px",
    borderRadius: 4,
    fontSize: 10,
    fontWeight: 600,
    textTransform: "uppercase",
  },
  compactDetail: {
    fontSize: 11,
    color: colors.textMuted,
  },
  footer: {
    textAlign: "center",
    padding: "20px",
    color: colors.textMuted,
    fontSize: 13,
    borderTop: `1px solid ${colors.borderPrimary}`,
    background: colors.bgSecondary,
  },
};
