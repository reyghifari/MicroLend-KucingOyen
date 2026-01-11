import React from "react";
import { styles } from "../styles";
import { colors } from "../constants";
import { truncateId } from "../utils";
import { Contract, LoanActive } from "../types";

interface ActiveLoansListProps {
    actives: Contract<LoanActive>[];
    onRepay: (cid: string, amount: string) => void;
    onMarkOverdue: (cid: string, currentDate: string) => void;
    onSeizeCollateral: (cid: string) => void;
}

export const ActiveLoansList: React.FC<ActiveLoansListProps> = ({
    actives,
    onRepay,
    onMarkOverdue,
    onSeizeCollateral,
}) => {
    return (
        <div style={styles.card}>
            <div style={styles.cardHeader}>
                <h2 style={styles.cardTitle}>Active Loans</h2>
                <span style={{ ...styles.badge, background: colors.infoBg, color: colors.info }}>
                    {actives.length}
                </span>
            </div>

            {actives.length === 0 ? (
                <div style={styles.emptyState}>
                    <span style={styles.emptyIcon}>💤</span>
                    <p>No active loans</p>
                </div>
            ) : (
                <div style={styles.loanList}>
                    {actives.map((c) => (
                        <div
                            key={c.contractId}
                            style={{
                                ...styles.loanItem,
                                borderLeftColor: c.payload.status === "Overdue" ? colors.danger : colors.info,
                            }}
                        >
                            <div style={styles.loanItemHeader}>
                                <span style={styles.loanAmount}>${c.payload.outstanding}</span>
                                <span
                                    style={{
                                        ...styles.statusBadge,
                                        background:
                                            c.payload.status === "Overdue" ? colors.dangerBg : colors.infoBg,
                                        color: c.payload.status === "Overdue" ? colors.danger : colors.info,
                                    }}
                                >
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
                                <button
                                    onClick={() => onRepay(c.contractId, c.payload.outstanding)}
                                    style={styles.successBtn}
                                >
                                    Repay
                                </button>
                                {c.payload.status !== "Overdue" && (
                                    <button
                                        onClick={() =>
                                            onMarkOverdue(c.contractId, new Date().toISOString().slice(0, 10))
                                        }
                                        style={styles.warningBtn}
                                    >
                                        Mark Overdue
                                    </button>
                                )}
                                {c.payload.status === "Overdue" && (
                                    <button
                                        onClick={() => onSeizeCollateral(c.contractId)}
                                        style={styles.dangerBtn}
                                    >
                                        Seize Collateral
                                    </button>
                                )}
                            </div>
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
};
