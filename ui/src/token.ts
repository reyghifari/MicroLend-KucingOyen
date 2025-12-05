import { SignJWT } from "jose";

export async function makeToken(party: string, ledgerId: string, applicationId: string, secret: string) {
  const payload: any = {
    scope: "daml_ledger_api",
    "https://daml.com/ledger-api": {
      ledgerId,
      applicationId,
      actAs: [party],
      readAs: [party]
    }
  };
  const key = new TextEncoder().encode(secret);
  const jwt = await new SignJWT(payload)
    .setProtectedHeader({ alg: "HS256", typ: "JWT" })
    .setIssuedAt()
    .setExpirationTime("1h")
    .sign(key);
  return jwt;
}

export async function makeListToken(
  ledgerId: string,
  applicationId: string,
  secret: string,
  readAs?: string[]
) {
  const payload: any = {
    scope: "daml_ledger_api",
    "https://daml.com/ledger-api": {
      ledgerId,
      applicationId,
      admin: true, // ← penting untuk akses party management
      readAs: readAs && readAs.length ? readAs : [],
    },
  };
  const key = new TextEncoder().encode(secret);
  const jwt = await new SignJWT(payload)
    .setProtectedHeader({ alg: "HS256", typ: "JWT" })
    .setIssuedAt()
    .setExpirationTime("1h")
    .sign(key);
  return jwt;
}


