import React from "react";
import { styles } from "../styles";
import { colors } from "../constants";

interface StatsRowProps {
    pendingCount: number;
    activeCount: number;
    completedCount: number;
    defaultedCount: number;
}

export const StatsRow: React.FC<StatsRowProps> = ({
    pendingCount,
    activeCount,
    completedCount,
    defaultedCount,
}) => {
    return (
        <div style={styles.statsRow}>
            <div style={styles.statCard}>
                <div style={{ ...styles.statNumber, color: colors.accent }}>{pendingCount}</div>
                <div style={styles.statLabel}>Pending</div>
            </div>
            <div style={styles.statCard}>
                <div style={{ ...styles.statNumber, color: colors.info }}>{activeCount}</div>
                <div style={styles.statLabel}>Active</div>
            </div>
            <div style={styles.statCard}>
                <div style={{ ...styles.statNumber, color: colors.success }}>{completedCount}</div>
                <div style={styles.statLabel}>Completed</div>
            </div>
            <div style={styles.statCard}>
                <div style={{ ...styles.statNumber, color: colors.danger }}>{defaultedCount}</div>
                <div style={styles.statLabel}>Defaulted</div>
            </div>
        </div>
    );
};
