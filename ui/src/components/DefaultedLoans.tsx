import React from "react";
import { styles } from "../styles";
import { colors } from "../constants";
import { Contract, LoanDefaulted } from "../types";

interface DefaultedLoansProps {
    defaulteds: Contract<LoanDefaulted>[];
}

export const DefaultedLoans: React.FC<DefaultedLoansProps> = ({ defaulteds }) => {
    return (
        <div style={styles.card}>
            <div style={styles.cardHeader}>
                <h2 style={styles.cardTitle}>Defaulted</h2>
                <span style={{ ...styles.badge, background: colors.dangerBg, color: colors.danger }}>
                    {defaulteds.length}
                </span>
            </div>

            {defaulteds.length === 0 ? (
                <div style={styles.emptyStateSmall}>
                    <span>🛡️</span>
                    <p>No defaults</p>
                </div>
            ) : (
                <div style={styles.loanListCompact}>
                    {defaulteds.map((c) => (
                        <div
                            key={c.contractId}
                            style={{ ...styles.loanItemCompact, borderLeftColor: colors.danger }}
                        >
                            <div style={styles.compactHeader}>
                                <span style={styles.compactAmount}>${c.payload.principal}</span>
                                <span
                                    style={{
                                        ...styles.compactBadge,
                                        background: colors.dangerBg,
                                        color: colors.danger,
                                    }}
                                >
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
    );
};
