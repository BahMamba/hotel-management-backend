export interface Hotel {
    id: number;
    name: string;
    address: string;
    city: string;
  }
  
  export interface Page<T> {
    content: T[];
    totalPages: number;
    totalElements: number;
    size: number;
    number: number;
  }