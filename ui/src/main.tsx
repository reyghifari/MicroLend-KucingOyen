import React from "react";
import { createRoot } from "react-dom/client";
import App from "./App";

class ErrorBoundary extends React.Component<{ children?: React.ReactNode }, { hasError: boolean; message: string }> {
    constructor(props: {}) {
        super(props);
        this.state = { hasError: false, message: "" };
    }
    static getDerivedStateFromError(err: any) {
        return { hasError: true, message: String(err?.message || err) };
    }
    componentDidCatch(err: any) {
        console.error(err);
    }
    render() {
        if (this.state.hasError) {
            return React.createElement("div", { style: { fontFamily: "system-ui", padding: 16 } },
                React.createElement("h2", null, "Terjadi error pada UI"),
                React.createElement("div", { style: { color: "#900" } }, this.state.message),
            );
        }
        return React.createElement(React.Fragment, null, this.props.children as any);
    }
}

const root = createRoot(document.getElementById("root")!);
root.render(
    <ErrorBoundary>
        <App />
    </ErrorBoundary>
);
