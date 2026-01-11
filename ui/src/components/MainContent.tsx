import React from "react";
import { styles } from "../styles";
import { StatsRow } from "./StatsRow";
import { LoanForm } from "./LoanForm";
import { LoanRequestsList } from "./LoanRequestsList";
import { ActiveLoansList } from "./ActiveLoansList";
import { CompletedLoans } from "./CompletedLoans";
import { DefaultedLoans } from "./DefaultedLoans";
import { Contract, LoanRequest, CollateralHold, LoanActive, LoanRepaid, LoanDefaulted, BorrowerFormState } from "../types";

interface MainContentProps {
    party: string;
    knownParties: string[];
    borrowerForm: BorrowerFormState;
    requests: Contract<LoanRequest>[];
    collaterals: Contract<CollateralHold>[];
    actives: Contract<LoanActive>[];
    repaids: Contract<LoanRepaid>[];
    defaulteds: Contract<LoanDefaulted>[];
    onFormChange: (form: BorrowerFormState) => void;
    onCreateRequest: () => void;
    onDepositCollateral: (cid: string) => void;
    onApprove: (requestCid: string, collateralCid: string) => void;
    onReject: (cid: string) => void;
    onRepay: (cid: string, amount: string) => void;
    onMarkOverdue: (cid: string, currentDate: string) => void;
    onSeizeCollateral: (cid: string) => void;
}

export const MainContent: React.FC<MainContentProps> = ({
    party,
    knownParties,
    borrowerForm,
    requests,
    collaterals,
    actives,
    repaids,
    defaulteds,
    onFormChange,
    onCreateRequest,
    onDepositCollateral,
    onApprove,
    onReject,
    onRepay,
    onMarkOverdue,
    onSeizeCollateral,
}) => {
    return (
        <div style={styles.mainContent}>
            <StatsRow
                pendingCount={requests.length}
                activeCount={actives.length}
                completedCount={repaids.length}
                defaultedCount={defaulteds.length}
            />

            <div style={styles.twoColumnLayout}>
                <div style={styles.leftColumn}>
                    <LoanForm
                        form={borrowerForm}
                        knownParties={knownParties}
                        currentParty={party}
                        onFormChange={onFormChange}
                        onCreate={onCreateRequest}
                    />
                </div>

                <div style={styles.rightColumn}>
                    <LoanRequestsList
                        requests={requests}
                        collaterals={collaterals}
                        onDepositCollateral={onDepositCollateral}
                        onApprove={onApprove}
                        onReject={onReject}
                    />

                    <ActiveLoansList
                        actives={actives}
                        onRepay={onRepay}
                        onMarkOverdue={onMarkOverdue}
                        onSeizeCollateral={onSeizeCollateral}
                    />

                    <div style={styles.twoColumnSmall}>
                        <CompletedLoans repaids={repaids} />
                        <DefaultedLoans defaulteds={defaulteds} />
                    </div>
                </div>
            </div>
        </div>
    );
};
