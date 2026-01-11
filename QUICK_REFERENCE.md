# 📋 Quick Reference Guide

## 🗂️ File Location Quick Lookup

**Looking for something?** Find it here:

| What | Where |
|------|-------|
| Button styles | `styles.ts` |
| Colors (dark theme) | `constants.ts` (colors object) |
| API calls | `apiService.ts` |
| Type definitions | `types.ts` |
| Helper functions | `utils.ts` |
| Main component | `App.tsx` |
| Header UI | `components/Header.tsx` |
| Loan form | `components/LoanForm.tsx` |
| List of requests | `components/LoanRequestsList.tsx` |
| Active loans | `components/ActiveLoansList.tsx` |
| Configuration | `constants.ts` |

---

## 🎯 Common Tasks

### 1. Change a Color
```typescript
// constants.ts
export const colors = {
  accent: "#6366f1",  // ← Change this
  // ...
}
```
**Result:** Updated everywhere instantly ✨

### 2. Add a New Component
```bash
# Create file
touch ui/src/components/MyComponent.tsx

# Add to components/index.ts
export { MyComponent } from "./MyComponent";

# Import in parent
import { MyComponent } from "./components";
```

### 3. Add a New Utility
```typescript
// utils.ts
export const myFunction = (arg: string): string => {
  return arg.toUpperCase();
};

// In any component
import { myFunction } from "../utils";
```

### 4. Call API
```typescript
// In App.tsx or component
try {
  await apiService.createRequest(party, payload, headers);
} catch (e) {
  setErrorMsg(e.message);
}
```

### 5. Add New Type
```typescript
// types.ts
export type MyType = {
  id: string;
  name: string;
};

// In component
import { MyType } from "../types";
const item: MyType = { id: "1", name: "test" };
```

---

## 📁 Import Examples

### Correct Imports ✅
```typescript
// From types
import { Contract, LoanRequest } from "../types";

// From components (barrel export)
import { Header, MainContent } from "../components";

// From utils
import { calculateCollateral } from "../utils";

// From constants
import { colors, ledgerId } from "../constants";

// From styles
import { styles } from "../styles";

// From apiService
import { apiService } from "../apiService";
```

### Wrong Imports ❌
```typescript
// DON'T do this:
import { Header } from "../components/Header";  // Too specific
import { calculateCollateral } from "../utils.ts";  // Wrong path
import { colors } from "../styles";  // Wrong file
```

---

## 🔄 Component Props Flow

```
App
├── Header
│   Props: { isConnected, party, onDisconnect }
│
├── ErrorBox
│   Props: { message, onClose }
│
├── ConnectView
│   Props: { party, knownParties, onPartyChange, onConnect, onRefreshParties }
│
└── MainContent
    Props: { party, knownParties, borrowerForm, requests, ... callbacks }
    ├── StatsRow
    │   Props: { pendingCount, activeCount, completedCount, defaultedCount }
    ├── LoanForm
    │   Props: { form, onFormChange, onCreate, ... }
    ├── LoanRequestsList
    │   Props: { requests, onDepositCollateral, onApprove, onReject }
    ├── ActiveLoansList
    │   Props: { actives, onRepay, onMarkOverdue, onSeizeCollateral }
    ├── CompletedLoans
    │   Props: { repaids }
    └── DefaultedLoans
        Props: { defaulteds }
```

---

## 🎨 Styling Workflow

### Step 1: Find existing style
```typescript
// In styles.ts
export const styles = {
  button: { ... },  // ← Found it
  // ...
};
```

### Step 2: Use in component
```typescript
import { styles } from "../styles";

<button style={styles.button}>Click me</button>
```

### Step 3: Need to modify?
```typescript
// Edit in styles.ts ONLY
export const styles = {
  button: {
    background: colors.accent,  // ← Change here
    padding: "16px",
  },
};

// All components using it update automatically!
```

---

## 🔗 Data Flow

```
App State (useState)
    ↓
Callbacks (useCallback)
    ↓
Pass to Components (as props)
    ↓
Components Call Callbacks
    ↓
State Updates
    ↓
Re-render
```

**Example:**
```typescript
// App.tsx - Define state
const [requests, setRequests] = useState([]);

// App.tsx - Define callback
const createRequest = useCallback(async () => {
  // ... API call
  setRequests([...requests, newRequest]);
}, [requests]);

// App.tsx - Pass to component
<LoanForm onCreate={createRequest} />

// LoanForm.tsx - Call callback
<button onClick={onCreate}>Create</button>
```

---

## 🧪 Testing Pattern

Once you add tests, structure would be:

```
components/
├── Header.tsx
├── __tests__/
│   └── Header.test.tsx
├── Footer.tsx
├── __tests__/
│   └── Footer.test.tsx
└── ...
```

**Example test:**
```typescript
import { render } from '@testing-library/react';
import { Header } from '../Header';

it('shows party name when connected', () => {
  const { getByText } = render(
    <Header isConnected={true} party="party-123" />
  );
  expect(getByText(/party-/)).toBeInTheDocument();
});
```

---

## 📊 Architecture at a Glance

```
┌─────────────────────────────────────┐
│          App.tsx                    │
│   (State Management & Logic)        │
└──────────┬──────────────────────────┘
           │
    ┌──────┴──────┬─────────┬─────────┐
    ↓             ↓         ↓         ↓
┌────────┐  ┌─────────┐ ┌──────┐ ┌────────┐
│Header  │  │Connect/ │ │Stats │ │Loan    │
│        │  │Main     │ │Forms │ │Lists   │
│Footer  │  │Content  │ │      │ │        │
└────────┘  └─────────┘ └──────┘ └────────┘
    │            │          │          │
    └────────────┴──────────┴──────────┘
                  │
        ┌─────────┴─────────┐
        ↓                   ↓
     Styles            Utilities
     & Colors          & Types
        ↓                   ↓
    constants.ts        types.ts
    styles.ts           utils.ts
                        apiService.ts
```

---

## ⚡ Performance Tips

### Memoize Expensive Computations
```typescript
const memoizedValue = useMemo(
  () => calculateCollateral(amount, rate),
  [amount, rate]
);
```

### Memoize Callbacks
```typescript
const handleClick = useCallback(() => {
  setCount(count + 1);
}, [count]);
```

### Memoize Components
```typescript
export const Header = React.memo(HeaderComponent);
```

---

## 🐛 Debugging Tips

### Check State
```typescript
console.log("Party:", party);
console.log("Requests:", requests);
console.log("Active:", actives);
```

### Check API Calls
```typescript
// In apiService.ts, add console.log
console.log("Calling API:", body);
```

### React DevTools
```bash
npm install react-devtools
# Then use "Profiler" tab to see component tree
```

---

## 📚 File Sizes

| File | Size | Purpose |
|------|------|---------|
| App.tsx | 470 LOC | Main logic |
| styles.ts | 350 LOC | Styling |
| constants.ts | 140 LOC | Config |
| apiService.ts | 80 LOC | API layer |
| types.ts | 48 LOC | Types |
| utils.ts | 19 LOC | Helpers |
| Components | 600 LOC | UI (11 files) |
| **Total** | **~1577 LOC** | **Organized** |

---

## 🎯 Common Patterns

### Props Drilling Solution
**Before:** Pass props through 3+ components
**After:** Use Context API (optional upgrade)

### State Management
**Current:** useState in App.tsx
**Future:** Consider Redux/Zustand if grows

### API Calls
**Current:** In components & App
**Better:** All in apiService.ts (done! ✅)

### Styling
**Current:** Centralized in styles.ts
**Future:** Could add Tailwind or CSS Modules

---

## 🚀 Deployment Checklist

- [ ] Check all TypeScript errors (`npx tsc --noEmit`)
- [ ] Test in different browsers
- [ ] Verify hardcoded data vs real API
- [ ] Check responsive design
- [ ] Test error states
- [ ] Verify all button actions work
- [ ] Test on mobile

---

## 📞 Quick Answers

**Q: Where are colors defined?**
A: `constants.ts` in the `colors` object

**Q: How do I add a new button?**
A: Add style to `styles.ts`, use in component

**Q: Where's the API code?**
A: `apiService.ts` has all API calls

**Q: How do I test this?**
A: Each component can be tested independently now!

**Q: Can I use this with Redux?**
A: Yes! Just wrap components with Redux provider

**Q: Is there a dark theme?**
A: Yes! Colors defined in `constants.ts`

---

**Need more help? Check the full documentation in ARCHITECTURE.md** 📖
