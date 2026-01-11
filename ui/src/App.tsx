import React, { useCallback, useEffect, useMemo, useState } from "react";
import { makeToken, makeListToken } from "./token";
import { Header } from "./components/Header";
import { Footer } from "./components/Footer";
import { ErrorBox } from "./components/ErrorBox";
import { ConnectView } from "./components/ConnectView";
import { MainContent } from "./components/MainContent";
import { styles } from "./styles";
import {
  Contract,
  LoanRequest,
  CollateralHold,
  LoanActive,
  LoanRepaid,
  LoanDefaulted,
  BorrowerFormState,
} from "./types";
import {
  ledgerId,
  applicationId,
  sharedSecret,
  HARDCODED_PARTIES,
  HARDCODED_REQUESTS,
  HARDCODED_COLLATERALS,
  HARDCODED_ACTIVES,
  HARDCODED_REPAIDS,
  HARDCODED_DEFAULTEDS,
  USE_HARDCODED_DATA,
  TR_LoanRequest,
  TR_LoanActive,
} from "./constants";
import { calculateCollateral } from "./utils";
import { apiService } from "./apiService";

export default function App() {
  const [party, setParty] = useState("");
  const [knownParties, setKnownParties] = useState<string[]>([]);
  const [errorMsg, setErrorMsg] = useState("");
  const [borrowerForm, setBorrowerForm] = useState<BorrowerFormState>({
    lender: "",
    amount: "",
    rate: "0.10",
    dueDate: "2025-12-31",
  });
  const [token, setToken] = useState("");
  const [isConnected, setIsConnected] = useState(false);

  const headers = useMemo(
    () => ({
      "Content-Type": "application/json",
      Authorization: token ? `Bearer ${token}` : "",
    }),
    [token]
  );

  const [requests, setRequests] = useState<Contract<LoanRequest>[]>([]);
  const [collaterals, setCollaterals] = useState<Contract<CollateralHold>[]>([]);
  const [actives, setActives] = useState<Contract<LoanActive>[]>([]);
  const [repaids, setRepaids] = useState<Contract<LoanRepaid>[]>([]);
  const [defaulteds, setDefaulteds] = useState<Contract<LoanDefaulted>[]>([]);

  const loadParties = useCallback(async () => {
    if (USE_HARDCODED_DATA) {
      setKnownParties(HARDCODED_PARTIES);
      setErrorMsg("");
      return;
    }

    try {
      const t = await makeListToken(ledgerId, applicationId, sharedSecret);
      const res = await fetch(`/api/v2/parties`, {
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
      const json = await apiService.loadContracts(headers);
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
      setCollaterals(
        all.filter((c) => c.templateId.endsWith(":MicroLend.Lending:CollateralHold")) as any
      );
      setActives(all.filter((c) => c.templateId.endsWith(":MicroLend.Lending:LoanActive")) as any);
      setRepaids(all.filter((c) => c.templateId.endsWith(":MicroLend.Lending:LoanRepaid")) as any);
      setDefaulteds(
        all.filter((c) => c.templateId.endsWith(":MicroLend.Lending:LoanDefaulted")) as any
      );
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

    try {
      await apiService.createRequest(party, payload, headers);
      await loadContracts();
      setBorrowerForm({ lender: "", amount: "", rate: "0.10", dueDate: "2025-12-31" });
    } catch (e: any) {
      setErrorMsg(e.message || "Failed to create LoanRequest");
    }
  }, [headers, party, borrowerForm, token, loadContracts, requests]);

  const depositCollateral = useCallback(
    async (cid: string) => {
      if (USE_HARDCODED_DATA) {
        const updatedRequests = requests.map((r) => {
          if (r.contractId === cid) {
            return { ...r, payload: { ...r.payload, collateralDeposited: true } };
          }
          return r;
        });

        const request = requests.find((r) => r.contractId === cid);
        if (request) {
          const collateralAmount = calculateCollateral(
            request.payload.amount,
            request.payload.rate
          );
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

      try {
        await apiService.exerciseChoice(
          party,
          TR_LoanRequest,
          cid,
          "DepositCollateral",
          {},
          headers
        );
        await loadContracts();
      } catch (e: any) {
        setErrorMsg(e.message || "Failed to deposit collateral");
      }
    },
    [headers, party, loadContracts, requests, collaterals]
  );

  const approve = useCallback(
    async (cid: string, collateralCid: string) => {
      if (USE_HARDCODED_DATA) {
        const request = requests.find((r) => r.contractId === cid);
        if (request) {
          const collateralAmount = calculateCollateral(
            request.payload.amount,
            request.payload.rate
          );
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
          setRequests(requests.filter((r) => r.contractId !== cid));
          setCollaterals(collaterals.filter((c) => c.contractId !== collateralCid));
        }
        return;
      }

      try {
        await apiService.exerciseChoice(
          party,
          TR_LoanRequest,
          cid,
          "Approve",
          { collateralCid },
          headers
        );
        await loadContracts();
      } catch (e: any) {
        setErrorMsg(e.message || "Failed to approve loan");
      }
    },
    [headers, party, loadContracts, requests, actives, collaterals]
  );

  const reject = useCallback(
    async (cid: string) => {
      if (USE_HARDCODED_DATA) {
        setRequests(requests.filter((r) => r.contractId !== cid));
        setCollaterals(collaterals.filter((c) => c.payload.loanRequestCid !== cid));
        return;
      }

      try {
        await apiService.exerciseChoice(party, TR_LoanRequest, cid, "Reject", {}, headers);
        await loadContracts();
      } catch (e: any) {
        setErrorMsg(e.message || "Failed to reject loan");
      }
    },
    [headers, party, loadContracts, requests, collaterals]
  );

  const repay = useCallback(
    async (cid: string, amount: string) => {
      if (USE_HARDCODED_DATA) {
        const active = actives.find((a) => a.contractId === cid);
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
          setActives(actives.filter((a) => a.contractId !== cid));
        }
        return;
      }

      try {
        await apiService.exerciseChoice(
          party,
          TR_LoanActive,
          cid,
          "Repay",
          { amount },
          headers
        );
        await loadContracts();
      } catch (e: any) {
        setErrorMsg(e.message || "Failed to repay loan");
      }
    },
    [headers, party, loadContracts, actives, repaids]
  );

  const markOverdue = useCallback(
    async (cid: string, currentDate: string) => {
      if (USE_HARDCODED_DATA) {
        const updatedActives = actives.map((a) => {
          if (a.contractId === cid) {
            return { ...a, payload: { ...a.payload, status: "Overdue" } };
          }
          return a;
        });
        setActives(updatedActives);
        return;
      }

      try {
        await apiService.exerciseChoice(
          party,
          TR_LoanActive,
          cid,
          "MarkOverdue",
          { currentDate },
          headers
        );
        await loadContracts();
      } catch (e: any) {
        setErrorMsg(e.message || "Failed to mark overdue");
      }
    },
    [headers, party, loadContracts, actives]
  );

  const seizeCollateral = useCallback(
    async (cid: string) => {
      if (USE_HARDCODED_DATA) {
        const active = actives.find((a) => a.contractId === cid);
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
          setActives(actives.filter((a) => a.contractId !== cid));
        }
        return;
      }

      try {
        await apiService.exerciseChoice(
          party,
          TR_LoanActive,
          cid,
          "SeizeCollateralFromLoan",
          {},
          headers
        );
        await loadContracts();
      } catch (e: any) {
        setErrorMsg(e.message || "Failed to seize collateral");
      }
    },
    [headers, party, loadContracts, actives, defaulteds]
  );

  return (
    <div style={styles.container}>
      <Header isConnected={isConnected} party={party} onDisconnect={handleDisconnect} />

      <ErrorBox message={errorMsg} onClose={() => setErrorMsg("")} />

      {!isConnected ? (
        <ConnectView
          party={party}
          knownParties={knownParties}
          onPartyChange={setParty}
          onConnect={handleConnect}
          onRefreshParties={loadParties}
        />
      ) : (
        <MainContent
          party={party}
          knownParties={knownParties}
          borrowerForm={borrowerForm}
          requests={requests}
          collaterals={collaterals}
          actives={actives}
          repaids={repaids}
          defaulteds={defaulteds}
          onFormChange={setBorrowerForm}
          onCreateRequest={createRequest}
          onDepositCollateral={depositCollateral}
          onApprove={approve}
          onReject={reject}
          onRepay={repay}
          onMarkOverdue={markOverdue}
          onSeizeCollateral={seizeCollateral}
        />
      )}

      <Footer />
    </div>
  );
}
