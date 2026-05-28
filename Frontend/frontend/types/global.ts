export interface ErrorResponse {
  status: number,
  error: string,
  message: string,
  timestamp: string,
  details?: Record<string, string>,
  stackTrace?: any[]
}

export interface JWTResponse {
  returnedToken: string,
  userAuthenticated: string
}

export type JWTUserRole = "OPERATOR" | "CONTRACTOR";
export type JWTType =  "operatorAuth" | "contractorAuth" | "changePassword";
// Based on how  backend generates the JWT
export interface JWTPayload {
  sub: string, // email
  name: string, // full name
  role: JWTUserRole,
  type: JWTType,
  iat: number,
  exp: number
}
