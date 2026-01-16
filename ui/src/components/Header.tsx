import React from "react";
import { styles } from "../styles";
import { truncateId } from "../utils";

interface HeaderProps {
    isConnected: boolean;
    party: string;
    onDisconnect: () => void;
}

export const Header: React.FC<HeaderProps> = ({ isConnected, party, onDisconnect }) => {
    return (
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
                    <button onClick={onDisconnect} style={styles.disconnectBtn}>
                        Disconnect
                    </button>
                </div>
            )}
        </header>
    );
};
