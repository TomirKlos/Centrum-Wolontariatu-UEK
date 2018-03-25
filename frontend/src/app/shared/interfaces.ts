export interface LoginCredentials {
  email: string;
  password: string;
}

export interface JWTToken {
  jwtToken: string;
}

export interface IResponseBody {
  status?: number;
  title?: string;
  detail?: string;
  message?: string;
}

export interface User {
  activated: boolean;
  authorities: string[];
  email: string;
  firstName: string;
  lastName: string;
  // resetDate:
}

export interface Pageable<T> {
  content: T[];
  first: boolean;
  last: boolean;
  number: number;
  numberOfElements: number;
  size: number;
  totalElements: number;
  totalPages: number;
}
