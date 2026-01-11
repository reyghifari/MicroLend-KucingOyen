import React from "react";
import { styles } from "../styles";

interface ErrorBoxProps {
    message: string;
    onClose: () => void;
}

export const ErrorBox: React.FC<ErrorBoxProps> = ({ message, onClose }) => {
    if (!message) return null;

    return (
        <div style={styles.errorBox}>
            <span>⚠</span> {message}
            <button onClick={onClose} style={styles.errorClose}>×</button>
        </div>
    );
};
