import React, { useState } from "react";

interface LandingPageProps {
    // Callback sekarang menerima partyId (string) untuk auto-connect
    onLoginSuccess: (partyId: string) => void;
}

export const LandingPage: React.FC<LandingPageProps> = ({ onLoginSuccess }) => {
    const [isSignUp, setIsSignUp] = useState(false);
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        if (email && password) {
            // --- SIMULASI BACKEND AUTH ---
            // Di dunia nyata, backend akan memvalidasi password dan mengembalikan Party ID user.
            // Di sini kita simulasi:
            // 1. Jika email mengandung "lender" (misal: bob@lender.com) -> Login sebagai Bob (Lender)
            // 2. Selain itu -> Login sebagai Alice (Borrower)

            // NOTE: Sesuaikan string ID ini dengan Party ID asli dari Canton Console/Navigator Anda jika tidak menggunakan Mock Data.
            let mockPartyId = "";

            if (email.toLowerCase().includes("lender")) {
                // ID Contoh untuk Lender (Bob)
                mockPartyId = "Bob::1220a...";
                console.log("Simulating Login as Lender (Bob)");
            } else {
                // ID Contoh untuk Borrower (Alice)
                mockPartyId = "Alice::1220b...";
                console.log("Simulating Login as Borrower (Alice)");
            }

            // Panggil callback ke App.tsx dengan Party ID
            onLoginSuccess(mockPartyId);
        } else {
            alert("Please enter email and password");
        }
    };

    return (
        <div style={landingStyles.container}>
            {/* Navbar */}
            <nav style={landingStyles.nav}>
                <div style={landingStyles.logo}>Canton MicroLend</div>
                <div style={landingStyles.navLinks}>
                    <a href="#how-it-works" style={landingStyles.link}>How it Works</a>
                    <a href="#tiers" style={landingStyles.link}>Reputation Tiers</a>
                </div>
            </nav>

            {/* Hero Section */}
            <header style={landingStyles.heroSection}>
                <div style={landingStyles.heroContent}>
                    <h1 style={landingStyles.title}>
                        Privacy-Preserving <br />
                        <span style={landingStyles.highlight}>Micro-Lending</span>
                    </h1>
                    <p style={landingStyles.subtitle}>
                        Secure loans with stablecoin collateral on the Canton Network.
                        Build your reputation and unlock lower interest rates automatically.
                    </p>
                    <div style={landingStyles.statsRow}>
                        <div style={landingStyles.stat}>
                            <h3>Privacy</h3>
                            <p>Auditable & Private</p>
                        </div>
                        <div style={landingStyles.stat}>
                            <h3>Automated</h3>
                            <p>Daml Workflows</p>
                        </div>
                    </div>
                </div>

                {/* Auth Card */}
                <div style={landingStyles.authCard}>
                    <h2 style={landingStyles.authTitle}>
                        {isSignUp ? "Create Account" : "Sign In to Dashboard"}
                    </h2>
                    <form onSubmit={handleSubmit} style={landingStyles.form}>
                        <div style={landingStyles.inputGroup}>
                            <label style={landingStyles.label}>Email Address</label>
                            <input
                                type="email"
                                placeholder="alice@borrower.com"
                                style={landingStyles.input}
                                value={email}
                                onChange={(e) => setEmail(e.target.value)}
                            />
                        </div>
                        <div style={landingStyles.inputGroup}>
                            <label style={landingStyles.label}>Password</label>
                            <input
                                type="password"
                                placeholder="••••••••"
                                style={landingStyles.input}
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                            />
                        </div>
                        <button type="submit" style={landingStyles.buttonPrimary}>
                            {isSignUp ? "Sign Up & Auto Connect" : "Sign In"}
                        </button>
                    </form>
                    <p style={landingStyles.switchAuth}>
                        {isSignUp ? "Already have an account?" : "New to MicroLend?"}{" "}
                        <span
                            onClick={() => setIsSignUp(!isSignUp)}
                            style={landingStyles.linkBtn}
                        >
                            {isSignUp ? "Sign In" : "Sign Up"}
                        </span>
                    </p>
                </div>
            </header>

            {/* Section: How It Works */}
            <section id="how-it-works" style={landingStyles.sectionAlt}>
                <div style={landingStyles.innerContainer}>
                    <h2 style={landingStyles.sectionTitle}>How It Works</h2>
                    <p style={landingStyles.sectionDesc}>Simple, transparent, and secure borrowing in 4 steps.</p>

                    <div style={landingStyles.stepsGrid}>
                        <div style={landingStyles.stepCard}>
                            <div style={landingStyles.stepNumber}>1</div>
                            <h3 style={landingStyles.stepTitle}>Connect Identity</h3>
                            <p style={landingStyles.stepText}>
                                Sign in and connect your Canton Party ID. Your privacy is protected by the network architecture.
                            </p>
                        </div>
                        <div style={landingStyles.stepCard}>
                            <div style={landingStyles.stepNumber}>2</div>
                            <h3 style={landingStyles.stepTitle}>Request Loan</h3>
                            <p style={landingStyles.stepText}>
                                Submit a request specifying amount and duration. Deposit stablecoin collateral to secure the deal.
                            </p>
                        </div>
                        <div style={landingStyles.stepCard}>
                            <div style={landingStyles.stepNumber}>3</div>
                            <h3 style={landingStyles.stepTitle}>Smart Approval</h3>
                            <p style={landingStyles.stepText}>
                                Lenders review and approve. Collateral is automatically locked in a Daml smart contract.
                            </p>
                        </div>
                        <div style={landingStyles.stepCard}>
                            <div style={landingStyles.stepNumber}>4</div>
                            <h3 style={landingStyles.stepTitle}>Repay & Earn</h3>
                            <p style={landingStyles.stepText}>
                                Repay on time to release collateral instantly. Consistent repayment boosts your Tier & lowers rates.
                            </p>
                        </div>
                    </div>
                </div>
            </section>

            {/* Section: Tiers */}
            <section id="tiers" style={landingStyles.section}>
                <div style={landingStyles.innerContainer}>
                    <h2 style={landingStyles.sectionTitle}>Reputation-Based Tier System</h2>
                    <p style={landingStyles.sectionDesc}>Consistent borrowers earn lower rates. Lenders stay protected.</p>
                    <div style={landingStyles.tierGrid}>
                        {[
                            { level: 1, rate: "10%", label: "Base Tier" },
                            { level: 2, rate: "9%", label: "Good" },
                            { level: 3, rate: "8%", label: "Reliable" },
                            { level: 4, rate: "7%", label: "Excellent" },
                            { level: 5, rate: "5%", label: "Elite" },
                        ].map((tier) => (
                            <div key={tier.level} style={landingStyles.tierCard}>
                                <div style={landingStyles.tierBadge}>Level {tier.level}</div>
                                <div style={landingStyles.tierRate}>{tier.rate}</div>
                                <div style={landingStyles.tierLabel}>Interest Rate</div>
                            </div>
                        ))}
                    </div>
                </div>
            </section>

            {/* Footer */}
            <footer style={landingStyles.footer}>
                © 2026 Canton MicroLend Prototype. Powered by Daml.
            </footer>
        </div>
    );
};

// Styles object
const landingStyles: { [key: string]: React.CSSProperties } = {
    container: {
        fontFamily: "'Inter', system-ui, sans-serif",
        color: "#333",
        backgroundColor: "#F9FAFB",
        minHeight: "100vh",
    },
    innerContainer: {
        maxWidth: "1200px",
        margin: "0 auto",
        padding: "0 20px",
    },
    nav: {
        display: "flex",
        justifyContent: "space-between",
        alignItems: "center",
        padding: "20px 40px",
        backgroundColor: "#ffffff",
        boxShadow: "0 2px 4px rgba(0,0,0,0.05)",
        position: "sticky",
        top: 0,
        zIndex: 100,
    },
    logo: {
        fontSize: "24px",
        fontWeight: "bold",
        color: "#2563EB",
    },
    navLinks: {
        display: "flex",
        gap: "24px",
    },
    link: {
        textDecoration: "none",
        color: "#4B5563",
        fontWeight: 500,
        fontSize: "14px",
    },
    heroSection: {
        display: "flex",
        flexWrap: "wrap",
        justifyContent: "center",
        alignItems: "center",
        padding: "80px 20px",
        background: "linear-gradient(135deg, #1E3A8A 0%, #3B82F6 100%)",
        color: "white",
    },
    heroContent: {
        flex: "1",
        maxWidth: "600px",
        padding: "20px",
    },
    title: {
        fontSize: "48px",
        lineHeight: "1.2",
        marginBottom: "20px",
        fontWeight: 800,
    },
    highlight: {
        color: "#93C5FD",
    },
    subtitle: {
        fontSize: "18px",
        opacity: 0.9,
        marginBottom: "40px",
        lineHeight: "1.6",
    },
    statsRow: {
        display: "flex",
        gap: "30px",
    },
    stat: {
        borderLeft: "3px solid #93C5FD",
        paddingLeft: "12px",
    },
    authCard: {
        backgroundColor: "white",
        padding: "40px",
        borderRadius: "16px",
        boxShadow: "0 20px 25px -5px rgba(0, 0, 0, 0.1)",
        width: "100%",
        maxWidth: "380px",
        color: "#333",
        margin: "20px",
    },
    authTitle: {
        fontSize: "24px",
        marginBottom: "24px",
        textAlign: "center",
        fontWeight: 700,
    },
    form: {
        display: "flex",
        flexDirection: "column",
        gap: "16px",
    },
    inputGroup: {
        display: "flex",
        flexDirection: "column",
        textAlign: "left",
    },
    label: {
        fontSize: "14px",
        fontWeight: 600,
        marginBottom: "8px",
        color: "#374151",
    },
    input: {
        padding: "12px",
        borderRadius: "8px",
        border: "1px solid #D1D5DB",
        fontSize: "16px",
        outline: "none",
        width: "100%",
        boxSizing: "border-box"
    },
    buttonPrimary: {
        padding: "12px",
        borderRadius: "8px",
        border: "none",
        backgroundColor: "#2563EB",
        color: "white",
        fontSize: "16px",
        fontWeight: 600,
        cursor: "pointer",
        marginTop: "8px",
        transition: "background-color 0.2s",
        width: "100%"
    },
    switchAuth: {
        textAlign: "center",
        marginTop: "16px",
        fontSize: "14px",
        color: "#6B7280",
    },
    linkBtn: {
        color: "#2563EB",
        cursor: "pointer",
        fontWeight: 600,
    },
    sectionAlt: {
        padding: "80px 0",
        backgroundColor: "#FFFFFF",
        textAlign: "center" as const,
    },
    section: {
        padding: "80px 0",
        backgroundColor: "#F9FAFB",
        textAlign: "center" as const,
    },
    sectionTitle: {
        fontSize: "36px",
        fontWeight: 800,
        marginBottom: "16px",
        color: "#111827",
    },
    sectionDesc: {
        fontSize: "18px",
        color: "#6B7280",
        marginBottom: "60px",
        maxWidth: "600px",
        marginLeft: "auto",
        marginRight: "auto",
    },
    stepsGrid: {
        display: "grid",
        gridTemplateColumns: "repeat(auto-fit, minmax(240px, 1fr))",
        gap: "30px",
        textAlign: "left" as const,
    },
    stepCard: {
        backgroundColor: "#F3F4F6",
        padding: "30px",
        borderRadius: "16px",
        position: "relative" as const,
    },
    stepNumber: {
        width: "40px",
        height: "40px",
        backgroundColor: "#2563EB",
        color: "white",
        borderRadius: "50%",
        display: "flex",
        alignItems: "center",
        justifyContent: "center",
        fontWeight: "bold",
        fontSize: "18px",
        marginBottom: "20px",
    },
    stepTitle: {
        fontSize: "20px",
        fontWeight: 700,
        marginBottom: "10px",
        color: "#1F2937",
    },
    stepText: {
        fontSize: "15px",
        color: "#4B5563",
        lineHeight: "1.6",
    },
    tierGrid: {
        display: "grid",
        gridTemplateColumns: "repeat(auto-fit, minmax(180px, 1fr))",
        gap: "20px",
    },
    tierCard: {
        backgroundColor: "white",
        padding: "24px",
        borderRadius: "12px",
        boxShadow: "0 4px 6px -1px rgba(0, 0, 0, 0.05)",
        borderTop: "4px solid #3B82F6",
        transition: "transform 0.2s",
    },
    tierBadge: {
        fontSize: "12px",
        fontWeight: "bold",
        color: "#3B82F6",
        textTransform: "uppercase",
        marginBottom: "8px",
        letterSpacing: "0.05em",
    },
    tierRate: {
        fontSize: "36px",
        fontWeight: 800,
        color: "#111827",
    },
    tierLabel: {
        fontSize: "14px",
        color: "#6B7280",
    },
    footer: {
        textAlign: "center" as const,
        padding: "40px",
        backgroundColor: "#111827",
        color: "#9CA3AF",
        fontSize: "14px",
    },
};