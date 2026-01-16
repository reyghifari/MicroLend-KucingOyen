# UI Structure - Refactored Code Organization

## File Structure

```
ui/src/
├── App.tsx              # Main application component (simplified)
├── main.tsx             # Entry point
├── token.ts             # Token utilities
├── types.ts             # TypeScript type definitions
├── constants.ts         # Constants, theme colors, hardcoded data
├── utils.ts             # Utility functions
├── styles.ts            # Centralized styling
├── apiService.ts        # API service layer
└── components/
    ├── index.ts         # Component exports
    ├── Header.tsx       # Header with navigation
    ├── Footer.tsx       # Footer component
    ├── ErrorBox.tsx     # Error notification
    ├── ConnectView.tsx  # Wallet connection screen
    ├── MainContent.tsx  # Main dashboard container
    ├── StatsRow.tsx     # Statistics cards
    ├── LoanForm.tsx     # Loan creation form
    ├── LoanRequestsList.tsx    # Pending requests list
    ├── ActiveLoansList.tsx     # Active loans list
    ├── CompletedLoans.tsx      # Completed loans list
    └── DefaultedLoans.tsx      # Defaulted loans list
```

## File Responsibilities

### Core Files
- **App.tsx** - Main component managing state and orchestrating functionality
- **types.ts** - All TypeScript interfaces and types
- **constants.ts** - Environment variables, colors, hardcoded data, template IDs
- **utils.ts** - Helper functions (calculateCollateral, truncateId, findCollateral)
- **styles.ts** - Centralized style object for consistent theming
- **apiService.ts** - Encapsulated API calls to Daml ledger

### Component Files
- **Header.tsx** - Top navigation with wallet connection status
- **Footer.tsx** - Simple footer
- **ErrorBox.tsx** - Error notification display
- **ConnectView.tsx** - Initial wallet connection screen with features
- **MainContent.tsx** - Dashboard layout composing all loan lists
- **StatsRow.tsx** - Four stat cards showing loan counts
- **LoanForm.tsx** - Form for creating new loan requests
- **LoanRequestsList.tsx** - List of pending requests with actions
- **ActiveLoansList.tsx** - List of active loans with repayment/overdue options
- **CompletedLoans.tsx** - Read-only list of repaid loans
- **DefaultedLoans.tsx** - Read-only list of defaulted loans

## Benefits of Refactoring

✅ **Separation of Concerns** - Each file has a single responsibility
✅ **Reusability** - Components and utilities can be used in other parts
✅ **Maintainability** - Easier to find and update code
✅ **Testability** - Smaller, focused components are easier to test
✅ **Scalability** - New features can be added without bloating files
✅ **Clean Main App** - App.tsx reduced from 1000+ lines to ~500 lines

## Import Conventions

```typescript
// Import from components using barrel export
import { Header, Footer, MainContent } from "./components";

// Import types
import { Contract, LoanRequest, BorrowerFormState } from "./types";

// Import constants
import { colors, USE_HARDCODED_DATA, ledgerId } from "./constants";

// Import utilities
import { calculateCollateral, truncateId } from "./utils";

// Import styles
import { styles } from "./styles";

// Import API service
import { apiService } from "./apiService";
```

## Architecture Overview

```
App Component (State Management)
    ↓
Splits into:
    ├── ConnectView (Login screen)
    └── MainContent (Dashboard)
        ├── Header
        ├── ErrorBox
        ├── StatsRow
        ├── LoanForm
        ├── LoanRequestsList
        ├── ActiveLoansList
        ├── CompletedLoans
        ├── DefaultedLoans
        └── Footer
```

## Adding New Features

To add a new feature, follow this pattern:

1. **Create new component** in `components/` folder
2. **Add types** to `types.ts` if needed
3. **Add constants** to `constants.ts` if needed
4. **Add styles** to `styles.ts`
5. **Export from** `components/index.ts`
6. **Integrate** into parent component
7. **Add API methods** to `apiService.ts` if needed

## Styling System

All colors and styles are centralized in `styles.ts` using the dark theme defined in `constants.ts`. To modify:

1. Update colors in `constants.ts`
2. Update style objects in `styles.ts`
3. Changes apply globally to all components

## API Integration

All API calls are abstracted in `apiService.ts`. Methods include:
- `loadContracts()` - Fetch active contracts from ledger
- `createRequest()` - Create new loan request
- `exerciseChoice()` - Execute contract choice (approve, repay, etc.)
