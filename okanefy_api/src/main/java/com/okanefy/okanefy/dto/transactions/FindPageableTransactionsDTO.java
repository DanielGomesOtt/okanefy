package com.okanefy.okanefy.dto.transactions;

import java.util.List;

public record FindPageableTransactionsDTO(
        List<TransactionDTO> transactions,
        int page,
        boolean first,
        boolean last,
        int numberElements,
        int size,
        Long totalElements,
        int totalPages
) {
}
