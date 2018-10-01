export interface LoginCredentials {
  email: string;
  password: string;
}

export interface JWTToken {
  jwtToken: string;
}

export interface User {
  activated: boolean;
  authorities: Authority[];
  email: string;
  firstName: string;
  lastName: string;
  // resetDate:
}

export interface Authority {
  name: string;
}

export interface VolunteerRequestVM {
  categories: string[];
  description: string;
  expirationDate: number;
  forStudents: boolean
  forTutors: boolean;
  pictures: Pictures[];
  title: string;
  types: string[];
  volunteersAmount: number;
  id: number;
}

export interface responseVolunteerRequestVM {
  description: string;
  user: User;
  id: number;
  seen: number;
}

export interface InvitationVolunteerRequestVM {
  description: string;
  volunteerAdId: number;
  volunteerRequestId: number;
  seen: number;
}

export interface Category {
  name: string;
}

export interface Certificate {
  id: number;
  certified: boolean;
  feedback: string;
  user: User;
  volunteerRequest: VolunteerRequestVM;
}

export interface Pictures{
  id: number;
  referenceToPicture: string;
}

export interface VolunteerAdVM {
  categories: string[];
  description: string;
  expirationDate: number;
  pictures: Pictures[];
  title: string;
  types: string[];
  id: number;
}

export interface Banner {
  description: string;
  title: string;
  referenceToPicture: string;
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
  value: string | number;
}

export class DialogData {
  volunteerRequest: VolunteerRequestVM;
  showApplyButton: boolean;
}
