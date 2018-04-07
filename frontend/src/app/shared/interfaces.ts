export interface LoginCredentials {
  email: string;
  password: string;
}

export interface JWTToken {
  jwtToken: string;
}

export interface User {
  activated: boolean;
  authorities: string[];
  email: string;
  firstName: string;
  lastName: string;
  // resetDate:
}

export interface VolunteerRequestVM {
  categories: string[];
  description: string;
  expirationDate: number;
  forStudents: boolean
  forTutors: boolean;
  images: string[];
  title: string;
  types: string[];
  volunteersAmount: 0;
}

export interface Page<T> {
  content: T[];
  first: boolean;
  last: boolean;
  number: number;
  numberOfElements: number;
  size: number;
  totalElements: number;
  totalPages: number;
}

export interface Param {
  name: string;
  value: string;
}
