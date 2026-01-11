import React from "react";
import { styles } from "../styles";
import { truncateId } from "../utils";

interface ConnectViewProps {
    party: string;
    knownParties: string[];
    onPartyChange: (party: string) => void;
    onConnect: () => void;
    onRefreshParties: () => void;
}

export const ConnectView: React.FC<ConnectViewProps> = ({
    party,
    knownParties,
    onPartyChange,
    onConnect,
    onRefreshParties,
}) => {
    return (
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
                        onChange={(e) => onPartyChange(e.target.value)}
                        style={styles.connectSelect}
                    >
                        <option value="">Select your party...</option>
                        {knownParties.map((id) => (
                            <option key={id} value={id}>
                                {truncateId(id)}
                            </option>
                        ))}
                    </select>
                    <button
                        onClick={onConnect}
                        disabled={!party}
                        style={{
                            ...styles.connectButton,
                            opacity: !party ? 0.5 : 1,
                            cursor: !party ? "not-allowed" : "pointer",
                        }}
                    >
                        Connect Wallet
                    </button>
                </div>
                <button onClick={onRefreshParties} style={styles.refreshLink}>
                    ↻ Refresh Parties
                </button>
            </div>

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
    );
};
