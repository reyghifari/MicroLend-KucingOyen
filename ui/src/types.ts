export type Contract<T> = {
    contractId: string;
    payload: T;
};

export type LoanRequest = {
    borrower: string;
    lender: string;
    amount: string;
    rate: string;
    dueDate: string;
    collateralDeposited: boolean;
};

export type CollateralHold = {
    owner: string;
    holder: string;
    amount: string;
    loanRequestCid: string;
};

export type LoanActive = {
    borrower: string;
    lender: string;
    principal: string;
    rate: string;
    dueDate: string;
    outstanding: string;
    status: string;
    collateralAmount: string;
    collateralCid: string;
};

export type LoanRepaid = {
    borrower: string;
    lender: string;
    principal: string;
    rate: string;
    paidAmount: string;
    dueDate: string;
    status: string;
    collateralReturned: boolean;
};

export type LoanDefaulted = {
    borrower: string;
    lender: string;
    principal: string;
    rate: string;
    dueDate: string;
    collateralSeized: string;
};

export type BorrowerFormState = {
    lender: string;
    amount: string;
    rate: string;
    dueDate: string;
};
