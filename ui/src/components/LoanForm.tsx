import React from "react";
import { styles } from "../styles";
import { calculateCollateral, truncateId } from "../utils";
import { BorrowerFormState } from "../types";

interface LoanFormProps {
    form: BorrowerFormState;
    knownParties: string[];
    currentParty: string;
    onFormChange: (form: BorrowerFormState) => void;
    onCreate: () => void;
}

export const LoanForm: React.FC<LoanFormProps> = ({
    form,
    knownParties,
    currentParty,
    onFormChange,
    onCreate,
}) => {
    return (
        <div style={styles.card}>
            <div style={styles.cardHeader}>
                <h2 style={styles.cardTitle}>Create Loan Request</h2>
            </div>

            {form.amount && (
                <div style={styles.collateralPreview}>
                    <span>🔒 Required Collateral:</span>
                    <strong>${calculateCollateral(form.amount, form.rate)}</strong>
                </div>
            )}

            <div style={styles.formGroup}>
                <label style={styles.label}>Lender</label>
                <select
                    value={form.lender}
                    onChange={(e) => onFormChange({ ...form, lender: e.target.value })}
                    style={styles.input}
                >
                    <option value="">Select lender...</option>
                    {knownParties
                        .filter((p) => p !== currentParty)
                        .map((id) => (
                            <option key={id} value={id}>
                                {truncateId(id)}
                            </option>
                        ))}
                </select>
            </div>

            <div style={styles.formRow}>
                <div style={styles.formGroup}>
                    <label style={styles.label}>Amount</label>
                    <input
                        value={form.amount}
                        onChange={(e) => onFormChange({ ...form, amount: e.target.value })}
                        style={styles.input}
                        placeholder="0.00"
                        type="number"
                    />
                </div>
                <div style={styles.formGroup}>
                    <label style={styles.label}>Rate</label>
                    <input
                        value={form.rate}
                        onChange={(e) => onFormChange({ ...form, rate: e.target.value })}
                        style={styles.input}
                        placeholder="0.10"
                    />
                </div>
            </div>

            <div style={styles.formGroup}>
                <label style={styles.label}>Due Date</label>
                <input
                    value={form.dueDate}
                    onChange={(e) => onFormChange({ ...form, dueDate: e.target.value })}
                    style={styles.input}
                    type="date"
                />
            </div>

            <button
                onClick={onCreate}
                disabled={!form.amount || !form.lender}
                style={{
                    ...styles.primaryButton,
                    opacity: !form.amount || !form.lender ? 0.5 : 1,
                }}
            >
                Create Request
            </button>
        </div>
    );
};
