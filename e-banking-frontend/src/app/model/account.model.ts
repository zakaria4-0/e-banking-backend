export interface Account {
  id: string
  type: string
  balance: number
  createdAt: string
  status: string
  customerDTO: Customer
  overDraft: number
  interestRate: number
}

export interface Customer {
  id: number
  name: string
  email: string
}
