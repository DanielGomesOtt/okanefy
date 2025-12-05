import { Button, Card, CardBody, CardFooter, CardHeader, Divider, Form, Input, Pagination, Select, SelectItem } from '@heroui/react';
import { useEffect, useState } from 'react'
import { BASE_URL } from '../../utils/constants';
import { FiPlus, FiSearch } from 'react-icons/fi';
import CreateTransactionForm from './CreateTransactionForm';

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
        {key: 'daily', name: 'Diario'},
        {key: 'monthly', name: 'Mensal'},
        {key: 'yearly', name: 'Anual'}
    ]

    const [errorMessage, setErrorMessage] = useState<string>("")
    const [totalPages, setTotalPages] = useState(1)
    const [currentPage, setCurrentPage] = useState(1)
    const [categories, setCategories] = useState<Category[]>([])
    const [paymentMethods, setPaymentMethods] = useState<PaymentMethod[]>([])
    const [category, setCategory] = useState<string>("all")
    const [paymentMethod, setPaymentMethod] = useState<string>("all")
    const [frequency, setFrequency] = useState<string>("all")
    const [isOpenCreateModal, setIsOpenCreateModal] = useState(false)
    const [description, setDescription] = useState("")
    const [initialDate, setInitialDate] = useState("")
    const [endDate, setEndDate] = useState("")



    async function getPaymentMethods() {
        try {
            const response = await fetch(`${BASE_URL}paymentMethod/transactions/${localStorage.getItem('id')}`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${localStorage.getItem('token')}`
                }
            })

            if(response.status !== 200) {
                const errorData = await response.json()
                throw new Error(errorData.message || "Erro ao tentar consultar formas de pagamento.")
            }

            const data = await response.json()
            setPaymentMethods(data)
        } catch (error: unknown) {
            console.log(error)
        }
    }

    async function getCategories() {
        try {
            const response = await fetch(`${BASE_URL}category/transactions/${localStorage.getItem('id')}`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${localStorage.getItem('token')}`
                }
            })

            if(response.status !== 200) {
                const errorData = await response.json()
                throw new Error(errorData.message || "Erro ao tentar consultar categorias.")
            }

            const data = await response.json()
            setCategories(data)
        } catch (error: unknown) {
            console.log(error)
        }
    }

    async function getTransactions(page: number = 1) {
        try {
            const params = new URLSearchParams({
                userId: String(localStorage.getItem('id')),
                page: String(page - 1),
                size: '25',
            })

            if(category !== "all") params.append('categoryId', category)
            if(paymentMethod !== "all") params.append('paymentMethodId', paymentMethod)
            if(frequency !== "all") params.append('frequency', frequency)
            if(description !== "") params.append('description', description)
            if(initialDate !== "") params.append('initialDate', initialDate)
            if(endDate !== "") params.append('endDate', endDate)
                
            const response = await fetch(`${BASE_URL}transactions?${params}`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${localStorage.getItem('token')}`
                }
            })

            if (response.status !== 200) {
                const errorData = await response.json()
                throw new Error(errorData.message || "Erro ao tentar consultar as transações.")
            }

            const data = await response.json()
            console.log(data)
            
        } catch (error) {
            if(error instanceof Error) {
                console.log(error.message)
            }
        }
    }

    useEffect(() => {
        getPaymentMethods()
        getCategories()
        getTransactions(1)
    }, [])
    
    return (
        <div className='w-4/5 h-5/5'>
            <Card className='w-full h-full'>
                <CardHeader>
                    <Form className="w-full" onSubmit={(e) => e.preventDefault()}>
                        <div className='text-center w-full'>
                            <span className='text-center text-red-500'>{errorMessage}</span>
                        </div>
                        <div className="w-full flex flex-wrap lg:flex-nowrap justify-center">
                            <div className="flex flex-wrap justify-center items-center gap-2 w-full lg:w-4/4">
                                <Input
                                    className="w-full lg:w-1/4"
                                    name="initial_date_input"
                                    type="date"
                                    label="Data inicial"
                                />

                                <Input
                                    className="w-full lg:w-1/4"
                                    name="end_date_input"
                                    type="date"
                                    label="Data final"
                                />

                                <Input
                                    className="w-full lg:w-1/4"
                                    name="description_input"
                                    type="text"
                                    placeholder='Pesquisar por descrição'
                                    label="Descrição"
                                />

                                <Select
                                    label="Frequência"
                                    className="w-full lg:w-1/4"
                                    selectedKeys={[frequency]}
                                >
                                    <SelectItem key="all">Todas</SelectItem>
                                    <>
                                        {
                                            typeFrequency.map((type) => (
                                                <SelectItem key={type.key}>{type.name}</SelectItem>
                                            ))
                                        }
                                    </>

                                </Select>

                                <Select
                                    label="Categoria"
                                    className="w-full lg:w-1/4"
                                    selectedKeys={[category]}
                                >
                                    <SelectItem key="all">Todas</SelectItem>
                                    
                                    <>
                                        {categories.map((c) => (
                                            <SelectItem key={c.id}>{c.name}</SelectItem>
                                        ))}
                                    </>

                                </Select>

                                <Select
                                    label="Forma de pagamento"
                                    className="w-full lg:w-1/4"
                                    selectedKeys={[paymentMethod]}
                                >
                                    <SelectItem key="all">Todas</SelectItem>

                                    <>
                                        {paymentMethods.map((p) => (
                                            <SelectItem key={p.id}>{p.name}</SelectItem>
                                        ))}
                                    </>

                                </Select>
                            </div>


                            <div className="flex flex-col gap-2 lg:gap-y-8 w-full lg:w-1/6">    
                                <Button
                                    className="bg-primary text-white w-full"
                                    startContent={<FiSearch size={18} />}
                                    onPress={() => getTransactions(1)}
                                >
                                    Pesquisar
                                </Button>

                                <Button
                                    className="bg-success text-white w-full"
                                    startContent={<FiPlus size={18} />}
                                    onPress={() => setIsOpenCreateModal(true)}
                                >
                                    Adicionar
                                </Button>
                            </div>
                        </div>    
                    </Form>
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
            <CreateTransactionForm isOpen={isOpenCreateModal} setIsOpen={setIsOpenCreateModal} parentPaymentMethods={paymentMethods} parentCategories={categories}/>
        </div>
    )
}

export default TransactionMainContent