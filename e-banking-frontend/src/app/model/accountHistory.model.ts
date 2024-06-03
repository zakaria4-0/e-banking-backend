export interface AccountDetails {
  accountId:     string;
  balance:       number;
  currentPage:   number;
  totalPages:    number;
  pageSize:      number;
  operationDTOS: Operation[];
}

export interface Operation {
  id:            number;
  operationDate: Date;
  amount:        number;
  type:          string;
  description:   string;
}
