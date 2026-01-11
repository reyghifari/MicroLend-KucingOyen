import React from "react";
import { styles } from "../styles";
import { colors } from "../constants";
import { Contract, LoanRepaid } from "../types";

interface CompletedLoansProps {
    repaids: Contract<LoanRepaid>[];
}

export const CompletedLoans: React.FC<CompletedLoansProps> = ({ repaids }) => {
    return (
        <div style={styles.card}>
            <div style={styles.cardHeader}>
                <h2 style={styles.cardTitle}>Completed</h2>
                <span style={{ ...styles.badge, background: colors.successBg, color: colors.success }}>
                    {repaids.length}
                </span>
            </div>

            {repaids.length === 0 ? (
                <div style={styles.emptyStateSmall}>
                    <span>🎯</span>
                    <p>No completed loans</p>
                </div>
            ) : (
                <div style={styles.loanListCompact}>
                    {repaids.map((c) => (
                        <div
                            key={c.contractId}
                            style={{ ...styles.loanItemCompact, borderLeftColor: colors.success }}
                        >
                            <div style={styles.compactHeader}>
                                <span style={styles.compactAmount}>${c.payload.paidAmount}</span>
                                <span
                                    style={{
                                        ...styles.compactBadge,
                                        background: colors.successBg,
                                        color: colors.success,
                                    }}
                                >
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
    );
};
