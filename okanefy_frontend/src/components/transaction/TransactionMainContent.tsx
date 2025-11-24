import { Card, CardBody, CardFooter, CardHeader, Divider, Pagination } from '@heroui/react';
import { useState } from 'react'

function TransactionMainContent() {

    interface Category {
        id: number
        name: string
        type: string
    }

    interface PaymentMethod {
        id: number
        name: string
        isInstallment: string
    }

    const typeFrequency = [
        {key: 'none', name: 'Nao frequente'},
        {key: 'daily', name: 'diario'},
        {key: 'monthly', name: 'mensal'},
        {key: 'yearly', name: 'anual'}
    ]

    const [errorMessage, setErrorMessage] = useState<string>("")
    const [totalPages, setTotalPages] = useState(1)
    const [currentPage, setCurrentPage] = useState(1)
    const [categories, setCategories] = useState<Category[]>([])
    const [paymentMethods, setPaymentMethods] = useState<PaymentMethod[]>([])

    
    return (
        <div>
            <div className='w-4/5 h-4/5'>
                <Card className='w-full h-full'>
                    <CardHeader>
                        
                    </CardHeader>
                    <Divider />
                    <CardBody>
                        
                    </CardBody>
                    <Divider />
                    <CardFooter className='flex justify-center'>
                        <Pagination isCompact showControls initialPage={1} total={totalPages}  page={currentPage} size="sm" className="sm:size-md"
                        onChange={(page) => {
                            setCurrentPage(page);
                        }}/>
                    </CardFooter>
                </Card>
            </div>
        </div>
    )
}

export default TransactionMainContent