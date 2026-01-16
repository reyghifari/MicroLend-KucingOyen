import {
    httpBaseUrl,
    applicationId,
    TR_LoanRequest,
    TR_LoanActive,
} from "./constants";
import { LoanRequest } from "./types";

export const apiService = {
    async loadContracts(headers: Record<string, string>) {
        try {
            const endRes = await fetch(`${httpBaseUrl}/v2/state/ledger-end`, {
                method: "GET",
                headers,
            });
            if (!endRes.ok) throw new Error(`ledger-end HTTP ${endRes.status}`);
            const { offset } = await endRes.json();

            const body = {
                filter: {
                    filtersByParty: {},
                    filtersForAnyParty: {
                        cumulative: [
                            {
                                identifierFilter: {
                                    WildcardFilter: {
                                        value: { includeCreatedEventBlob: true },
                                    },
                                },
                            },
                        ],
                    },
                },
                verbose: false,
                activeAtOffset: offset,
                eventFormat: null,
            };

            const res = await fetch(`${httpBaseUrl}/v2/state/active-contracts`, {
                method: "POST",
                headers,
                body: JSON.stringify(body),
            });

            if (!res.ok) throw new Error(`active-contracts HTTP ${res.status}`);
            return await res.json();
        } catch (e: any) {
            throw new Error(e.message || "Failed to load contracts");
        }
    },

    async createRequest(
        party: string,
        payload: LoanRequest,
        headers: Record<string, string>
    ) {
        const body = {
            commandId: crypto.randomUUID(),
            applicationId,
            actAs: [party],
            readAs: [],
            commands: [
                {
                    CreateCommand: {
                        templateId: TR_LoanRequest,
                        createArguments: payload,
                    },
                },
            ],
        };

        const res = await fetch(
            `${httpBaseUrl}/v2/commands/submit-and-wait-for-transaction-tree`,
            { method: "POST", headers, body: JSON.stringify(body) }
        );
        if (!res.ok) throw new Error(`Create failed (${res.status})`);
    },

    async exerciseChoice(
        party: string,
        templateId: string,
        contractId: string,
        choice: string,
        choiceArgument: any,
        headers: Record<string, string>
    ) {
        const body = {
            commandId: crypto.randomUUID(),
            applicationId,
            actAs: [party],
            readAs: [],
            commands: [
                {
                    ExerciseCommand: {
                        templateId,
                        contractId,
                        choice,
                        choiceArgument,
                    },
                },
            ],
        };

        const res = await fetch(
            `${httpBaseUrl}/v2/commands/submit-and-wait-for-transaction-tree`,
            { method: "POST", headers, body: JSON.stringify(body) }
        );
        if (!res.ok) throw new Error(`${choice} failed (${res.status})`);
    },
};
