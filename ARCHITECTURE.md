```
╔════════════════════════════════════════════════════════════════════════════╗
║                    MicroLend UI - REFACTORED STRUCTURE                      ║
╚════════════════════════════════════════════════════════════════════════════╝

📂 ui/src/
│
├── 📄 App.tsx                    [MAIN COMPONENT - 470 lines]
│   └─ Manages state & orchestrates components
│
├── 📄 types.ts                   [TYPE DEFINITIONS - 48 lines]
│   ├── Contract<T>
│   ├── LoanRequest, CollateralHold
│   ├── LoanActive, LoanRepaid, LoanDefaulted
│   └── BorrowerFormState
│
├── 📄 constants.ts               [CONFIGURATION - 140 lines]
│   ├── API endpoints (ledgerId, applicationId, etc.)
│   ├── Template IDs
│   ├── HARDCODED_PARTIES
│   ├── HARDCODED_REQUESTS, COLLATERALS, ACTIVES, etc.
│   └── colors (dark theme)
│
├── 📄 utils.ts                   [UTILITIES - 19 lines]
│   ├── calculateCollateral(amount, rate)
│   ├── truncateId(id)
│   └── findCollateralForRequest(cid, collaterals)
│
├── 📄 styles.ts                  [STYLING - 350 lines]
│   └── All CSS-in-JS style objects
│
├── 📄 apiService.ts              [API LAYER - 80 lines]
│   ├── loadContracts(headers)
│   ├── createRequest(party, payload, headers)
│   └── exerciseChoice(party, templateId, contractId, choice, arg, headers)
│
└── 📂 components/                [11 FOCUSED COMPONENTS]
    │
    ├── 📄 index.ts               [BARREL EXPORT]
    │
    ├── 📄 Header.tsx             [Navigation bar with wallet info]
    │   Props: { isConnected, party, onDisconnect }
    │
    ├── 📄 Footer.tsx             [Simple footer]
    │   Props: (none)
    │
    ├── 📄 ErrorBox.tsx           [Error notification]
    │   Props: { message, onClose }
    │
    ├── 📄 ConnectView.tsx        [Wallet connection screen]
    │   Props: { party, knownParties, onPartyChange, onConnect, onRefreshParties }
    │   Contains: Features grid
    │
    ├── 📄 MainContent.tsx        [Dashboard container]
    │   Props: All loan data & callbacks
    │   Contains: All loan components
    │
    ├── 📄 StatsRow.tsx           [Statistics display]
    │   Props: { pendingCount, activeCount, completedCount, defaultedCount }
    │   Displays: 4 stat cards
    │
    ├── 📄 LoanForm.tsx           [Create new loan]
    │   Props: { form, knownParties, currentParty, onFormChange, onCreate }
    │   Features: Collateral preview
    │
    ├── 📄 LoanRequestsList.tsx   [Pending requests]
    │   Props: { requests, collaterals, onDepositCollateral, onApprove, onReject }
    │   Features: Status badges, actions
    │
    ├── 📄 ActiveLoansList.tsx    [Active loans]
    │   Props: { actives, onRepay, onMarkOverdue, onSeizeCollateral }
    │   Features: Overdue status, actions
    │
    ├── 📄 CompletedLoans.tsx     [Repaid loans (read-only)]
    │   Props: { repaids }
    │   Displays: Compact list
    │
    └── 📄 DefaultedLoans.tsx     [Defaulted loans (read-only)]
        Props: { defaulteds }
        Displays: Compact list with seized amounts

╔════════════════════════════════════════════════════════════════════════════╗
║                         IMPORT HIERARCHY                                    ║
╚════════════════════════════════════════════════════════════════════════════╝

App.tsx (Entry Point)
    │
    ├─→ imports: types, constants, utils, styles, apiService
    │
    └─→ renders: Header, ErrorBox, (ConnectView | MainContent), Footer
            │
            ├─→ ConnectView
            │   └─→ styles, colors, truncateId
            │
            └─→ MainContent
                ├─→ StatsRow
                ├─→ LoanForm
                │   └─→ styles, truncateId, calculateCollateral
                ├─→ LoanRequestsList
                │   └─→ styles, colors, utils
                ├─→ ActiveLoansList
                │   └─→ styles, colors, truncateId
                ├─→ CompletedLoans
                │   └─→ styles, colors
                └─→ DefaultedLoans
                    └─→ styles, colors

╔════════════════════════════════════════════════════════════════════════════╗
║                         SEPARATION OF CONCERNS                              ║
╚════════════════════════════════════════════════════════════════════════════╝

┌─ DATA LAYER ────────────────────┐
│ • constants.ts (hardcoded data) │
│ • apiService.ts (API calls)     │
└─────────────────────────────────┘
         ↓
┌─ STATE LAYER ───────────────────┐
│ • App.tsx (useState, callbacks)  │
└─────────────────────────────────┘
         ↓
┌─ VIEW LAYER ────────────────────┐
│ • components/ (UI rendering)    │
│ • styles.ts (consistent styling)│
│ • types.ts (type safety)        │
│ • utils.ts (shared logic)       │
└─────────────────────────────────┘

╔════════════════════════════════════════════════════════════════════════════╗
║                              QUICK STATS                                    ║
╚════════════════════════════════════════════════════════════════════════════╝

BEFORE:
  • App.tsx: 1000+ lines (monolithic)
  • 1 file total
  • Hard to navigate
  • Difficult to test
  • Prone to merge conflicts

AFTER:
  • App.tsx: 470 lines (clean)
  • 19 files (organized)
  • Easy to navigate
  • Easy to test
  • Minimal merge conflicts
  
IMPROVEMENTS:
  ✅ 52% reduction in App.tsx size
  ✅ 19 focused files vs 1 monolith
  ✅ 11 reusable components
  ✅ Single Responsibility Principle
  ✅ Easier maintenance & testing
  ✅ Better code reusability
  ✅ Scalable architecture

╔════════════════════════════════════════════════════════════════════════════╗
║                          QUICK REFERENCE                                    ║
╚════════════════════════════════════════════════════════════════════════════╝

Find something?  Where to look:
─────────────────────────────────────────────────────
Colors           → constants.ts (colors object)
Styles           → styles.ts
Types            → types.ts
API calls        → apiService.ts
Constants        → constants.ts
Utilities        → utils.ts
UI Components    → components/*.tsx
Main logic       → App.tsx

Need to modify:
─────────────────────────────────────────────────────
Styling          → styles.ts (one central location)
Colors           → constants.ts (colors object)
API behavior     → apiService.ts
Component layout → components/MainContent.tsx
Types            → types.ts

═══════════════════════════════════════════════════════════════════════════════
                                  READY TO USE! 🚀
═══════════════════════════════════════════════════════════════════════════════
```
