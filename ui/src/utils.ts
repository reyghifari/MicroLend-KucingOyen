import { Contract, CollateralHold } from "./types";

export const calculateCollateral = (amount: string, rate: string): string => {
    const amt = parseFloat(amount) || 0;
    const rt = parseFloat(rate) || 0;
    return (amt + amt * rt).toFixed(2);
};

export const truncateId = (id: string): string => {
    if (id.length > 20) return `${id.slice(0, 10)}...${id.slice(-8)}`;
    return id;
};

export const findCollateralForRequest = (
    requestCid: string,
    collaterals: Contract<CollateralHold>[]
): Contract<CollateralHold> | undefined => {
    return collaterals.find(c => c.payload.loanRequestCid === requestCid);
};
