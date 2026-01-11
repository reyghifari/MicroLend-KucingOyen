import React from "react";
import { styles } from "../styles";
import { colors } from "../constants";
import { calculateCollateral, truncateId, findCollateralForRequest } from "../utils";
import { Contract, LoanRequest, CollateralHold } from "../types";

interface LoanRequestsListProps {
    requests: Contract<LoanRequest>[];
    collaterals: Contract<CollateralHold>[];
    onDepositCollateral: (cid: string) => void;
    onApprove: (requestCid: string, collateralCid: string) => void;
    onReject: (cid: string) => void;
}

export const LoanRequestsList: React.FC<LoanRequestsListProps> = ({
    requests,
    collaterals,
    onDepositCollateral,
    onApprove,
    onReject,
}) => {
    return (
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
                    {requests.map((c) => {
                        const collateral = findCollateralForRequest(c.contractId, collaterals);
                        const collateralRequired = calculateCollateral(c.payload.amount, c.payload.rate);

                        return (
                            <div
                                key={c.contractId}
                                style={{
                                    ...styles.loanItem,
                                    borderLeftColor: c.payload.collateralDeposited ? colors.info : colors.warning,
                                }}
                            >
                                <div style={styles.loanItemHeader}>
                                    <span style={styles.loanAmount}>${c.payload.amount}</span>
                                    <span
                                        style={{
                                            ...styles.statusBadge,
                                            background: c.payload.collateralDeposited ? colors.infoBg : colors.warningBg,
                                            color: c.payload.collateralDeposited ? colors.info : colors.warning,
                                        }}
                                    >
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
                                        <button onClick={() => onDepositCollateral(c.contractId)} style={styles.warningBtn}>
                                            Deposit ${collateralRequired}
                                        </button>
                                    )}
                                    {c.payload.collateralDeposited && collateral && (
                                        <button
                                            onClick={() => onApprove(c.contractId, collateral.contractId)}
                                            style={styles.successBtn}
                                        >
                                            Approve
                                        </button>
                                    )}
                                    <button onClick={() => onReject(c.contractId)} style={styles.dangerBtn}>
                                        Reject
                                    </button>
                                </div>
                            </div>
                        );
                    })}
                </div>
            )}
        </div>
    );
};
