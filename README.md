# 🐱 Canton MicroLend

[![Canton Network](https://img.shields.io/badge/Built%20on-Canton%20Network-blue)](https://www.canton.network/)
[![Daml](https://img.shields.io/badge/Smart%20Contracts-Daml-green)](https://www.digitalasset.com/daml)
[![Track](https://img.shields.io/badge/Track-Lending%2C%20Borrowing%20%26%20Yield-orange)]()

> **Private Micro-Lending Made Simple.**

Canton MicroLend is a privacy-preserving micro-lending prototype built on the Canton Network. The platform enables borrowers to submit small fixed-rate loan requests with stablecoin collateral, while lenders can review and approve them through automated Daml workflows.

**Team:** Kucing Oyen 🐈

---

## 📋 Table of Contents

- [Overview](#-overview)
- [Key Features](#-key-features)
- [Problem Statement](#-problem-statement)
- [Solution](#-solution)
- [Workflow](#-workflow)
- [Borrower Tier System](#-borrower-tier-system)
- [Tech Stack](#-tech-stack)
- [Getting Started](#-getting-started)
- [Project Structure](#-project-structure)
- [Demo](#-demo)
- [Future Development](#-future-development)
- [Team](#-team)
- [License](#-license)

---

## 🌟 Overview

Canton MicroLend delivers a micro-lending system that is:
- **Privacy-preserving** - Only involved parties can view loan details
- **Collateral-backed** - Every loan is secured with stablecoin collateral
- **Reputation-based** - Tier system that rewards consistent borrowers
- **Fully auditable** - All actions are recorded and auditable

---

## ✨ Key Features

| Feature | Description |
|---------|-------------|
| 🔒 **Stablecoin Collateral** | Loans are secured with stablecoin for lender protection |
| ⭐ **Reputation Tiering** | Tier system from Starter → Reliable → Trusted → Prime |
| 💰 **Fair Interest System** | Interest discounts for borrowers without reducing lender yield |
| 🔐 **Privacy by Design** | Visibility limited to relevant borrower and lender only |
| 📊 **Audit Trail** | Complete action history for compliance and reporting |
| ⚡ **Automated Workflows** | Loan lifecycle automated through Daml smart contracts |

---

## 🎯 Problem Statement

Small loans are often:
- ❌ Tracked manually, creating unclear status
- ❌ Subject to communication gaps and weak auditability
- ❌ Risky for lenders without trust signals or collateral
- ❌ Lacking incentives for borrowers to build long-term creditworthiness
- ❌ Not combining privacy, automation, and risk-based incentives effectively

---

## 💡 Solution

### Solution Flow


### Concept Highlights

- ✅ Stablecoin collateral for secure lending
- ✅ Borrower reputation tiering (4 levels)
- ✅ Tier determines interest discount without reducing lender yield
- ✅ Collateral requirements adjust by tier
- ✅ Privacy-preserving visibility for borrower and lender only
- ✅ Fully auditable action history for compliance

---

## 🔄 Workflow




### Prototype Workflow Overview

1. **Connect** - Users connect with their Party ID
2. **Create Request** - Borrowers create loan requests (lender, amount, interest rate, due date, collateral)
3. **Review** - Lenders see requests with collateral value and borrower tier
4. **Approve** - After approval, collateral becomes locked within the `LoanActive` contract
5. **Repay** - Borrowers repay through the interface, releasing collateral
6. **Complete** - Loan moves to completed, reputation score updates

---

## 🏆 Borrower Tier System

A tier system that is **fair for both sides**:

### Tier Structure

| Tier | Level | Interest Discount | Collateral LTV |
|------|-------|-------------------|----------------|
| 🥉 Tier 1 | **Starter** | Base Rate (0%) | 100% |
| 🥈 Tier 2 | **Reliable** | -5% | 90% |
| 🥇 Tier 3 | **Trusted** | -10% | 85% |
| 💎 Tier 4 | **Prime** | -15% | 80% |

### Risk-Weighted Reputation Score

Score is calculated based on:
- 📈 Repayment behavior
- 💰 Collateral quality
- ⏰ On-time performance
- 🔄 Loan consistency

### Fairness Guarantee


---

## 🛠 Tech Stack

| Layer | Technology |
|-------|------------|
| **Blockchain** | Canton Network |
| **Smart Contracts** | Daml |
| **Privacy** | Canton's privacy-preserving architecture |
| **Collateral** | Stablecoin-based |

---

## 🚀 Getting Started

### Prerequisites

- [Daml SDK](https://docs.daml.com/getting-started/installation.html)
- [Canton](https://www.canton.network/developers)
- Node.js (for frontend, if applicable)

### Installation

```bash
# Clone repository
git clone https://github.com/[your-username]/canton-microlend.git
cd canton-microlend

# Install Daml dependencies
daml build

# Start Canton sandbox
daml start

# (Optional) Start frontend
cd ui
npm install
npm start
