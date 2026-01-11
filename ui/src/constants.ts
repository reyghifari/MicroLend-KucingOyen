import { Contract, LoanRequest, CollateralHold, LoanActive, LoanRepaid, LoanDefaulted } from "./types";

export const httpBaseUrl = (import.meta as any).env?.VITE_HTTP_BASE_URL || "/api";
export const ledgerId = (import.meta as any).env?.VITE_LEDGER_ID || "sandbox";
export const applicationId = (import.meta as any).env?.VITE_APPLICATION_ID || "microlend-ui";
export const sharedSecret = (import.meta as any).env?.VITE_SHARED_SECRET || "secret";

export const TR_LoanRequest = "MicroLend.Lending:LoanRequest";
export const TR_LoanActive = "MicroLend.Lending:LoanActive";
export const TR_LoanRepaid = "MicroLend.Lending:LoanRepaid";
export const TR_CollateralHold = "MicroLend.Lending:CollateralHold";
export const TR_LoanDefaulted = "MicroLend.Lending:LoanDefaulted";

export const HARDCODED_PARTIES = [
    "party::borrower-1::1220abc123",
    "party::borrower-2::1220def456",
    "party::lender-1::1220ghi789",
    "party::lender-2::1220jkl012",
];

export const HARDCODED_REQUESTS: Contract<LoanRequest>[] = [
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

export const HARDCODED_COLLATERALS: Contract<CollateralHold>[] = [
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

export const HARDCODED_ACTIVES: Contract<LoanActive>[] = [
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

export const HARDCODED_REPAIDS: Contract<LoanRepaid>[] = [
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

export const HARDCODED_DEFAULTEDS: Contract<LoanDefaulted>[] = [
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

export const USE_HARDCODED_DATA = true;

export const colors = {
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
