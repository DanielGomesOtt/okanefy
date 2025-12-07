import { Button, Card, CardBody, CardFooter, CardHeader, Divider, Form, Input, Pagination, Select, SelectItem, Table, TableBody, TableCell, TableColumn, TableHeader, TableRow } from '@heroui/react';
import { useEffect, useState } from 'react'
import { BASE_URL } from '../../utils/constants';
import { FiEdit2, FiPlus, FiSearch, FiTrash2 } from 'react-icons/fi';
import CreateTransactionForm from './CreateTransactionForm';
import formatDate from '../../utils/formatDate';

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

    interface Transaction {
        id: number
        description: string
        initial_date: string
        end_date: string
        amount: number
        installment: number
        frequency: string
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
    const [transactions, setTransactions] = useState<Transaction[]>([])



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
        
            setTransactions(data.transactions)
            
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
                                    onChange={(e) => setInitialDate(e.target.value)}
                                />

                                <Input
                                    className="w-full lg:w-1/4"
                                    name="end_date_input"
                                    type="date"
                                    label="Data final"
                                    onChange={(e) => setEndDate(e.target.value)}
                                />

                                <Input
                                    className="w-full lg:w-1/4"
                                    name="description_input"
                                    type="text"
                                    placeholder='Pesquisar por descrição'
                                    label="Descrição"
                                    onChange={(e) => setDescription(e.target.value)}
                                />

                                <Select
                                    label="Frequência"
                                    className="w-full lg:w-1/4"
                                    selectedKeys={[frequency]}
                                    onSelectionChange={(keys) => {
                                        const key = Array.from(keys)[0];
                                        setFrequency(key.toString());
                                    }}
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
                                    onSelectionChange={(keys) => {
                                        const key = Array.from(keys)[0];
                                        setCategory(key.toString());
                                    }}
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
                                    onSelectionChange={(keys) => {
                                        const key = Array.from(keys)[0];
                                        setPaymentMethod(key.toString());
                                    }}
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
                    <Table aria-labelledby='Tabela de formaa de pagamento'>
                        <TableHeader>
                            <TableColumn key="id" className='text-bold text-blue-500 text-center'>ID</TableColumn>
                            <TableColumn key="description" className='text-bold text-blue-500 text-center'>Descrição</TableColumn>
                            <TableColumn key="initial_date" className='text-bold text-blue-500 text-center'>Data inicial</TableColumn>
                            <TableColumn key="end_date" className='text-bold text-blue-500 text-center'>Data final</TableColumn>
                            <TableColumn key="installments" className='text-bold text-blue-500 text-center'>Parcelas</TableColumn>
                            <TableColumn key="frequency" className='text-bold text-blue-500 text-center'>Frequência</TableColumn>
                            <TableColumn key="amount" className='text-bold text-blue-500 text-center'>Valor</TableColumn>
                            <TableColumn key="payments" className='text-bold text-blue-500 text-center'>Pagamentos</TableColumn>
                            <TableColumn key="edit" className='text-bold text-blue-500 text-center'>Editar</TableColumn>
                            <TableColumn key="delete" className='text-bold text-blue-500 text-center'>Excluir</TableColumn>
                        </TableHeader>

                        <TableBody items={transactions}>
                            {(item: any) => (
                                <TableRow key={item.id}>
                                    <TableCell className="text-center">{item.id}</TableCell>
                                    <TableCell className="text-center">{item.description}</TableCell>
                                    <TableCell className="text-center">
                                        {formatDate(item.initial_date)}
                                    </TableCell>

                                    <TableCell className="text-center">
                                        {formatDate(item.end_date)}
                                    </TableCell>
                                    <TableCell className="text-center">{item.number_installments}</TableCell>
                                    <TableCell className="text-center">
                                        {typeFrequency.find(f => f.key === item.frequency)?.name || "—"}
                                    </TableCell>

                                    <TableCell className="text-center">R$ {String(item.amount.toFixed(2)).replace(".", ",")}</TableCell>

                                    <TableCell className="flex justify-center w-full">
                                        <Select
                                            selectedKeys={item.payment_methods?.map((pm: any) => String(pm.payment_method_id)) || []}
                                            selectionMode="multiple"
                                            className="w-40"
                                            aria-label='payments_method_select'
                                        >
                                            {item.payment_methods?.map((pm: any) => {
                                                const method = paymentMethods.find(m => m.id === pm.payment_method_id)

                                                return (
                                                    <SelectItem key={pm.payment_method_id}>
                                                        {method ? method.name : `ID ${pm.payment_method_id}`}
                                                    </SelectItem>
                                                )
                                            })}
                                        </Select>
                                    </TableCell>


                                    <TableCell className="text-center">
                                        <Button
                                            isIconOnly
                                            size="sm"
                                            color="primary"
                                            variant="bordered"
                                            className="transition-all duration-200 hover:bg-primary hover:text-white"
                                            onPress={() => {
                                                
                                            }}
                                        >
                                            <FiEdit2 className="w-4 h-4" />
                                        </Button>
                                    </TableCell>

                                    <TableCell className="text-center">
                                        <Button
                                            isIconOnly
                                            size="sm"
                                            color="danger"
                                            variant="bordered"
                                            className="transition-all duration-200 hover:bg-danger hover:variant-solid hover:text-white"
                                            onPress={() => {
                                                
                                            }}
                                        >
                                            <FiTrash2 className="w-4 h-4" />
                                        </Button>
                                    </TableCell>
                                </TableRow>
                            )}
                        </TableBody>
                    </Table>
                </CardBody>
                <Divider />
                <CardFooter className='flex justify-center'>
                    <Pagination isCompact showControls initialPage={1} total={totalPages}  page={currentPage} size="sm" className="sm:size-md"
                    onChange={(page) => {
                        setCurrentPage(page);
                    }}/>
                </CardFooter>
            </Card>
            <CreateTransactionForm isOpen={isOpenCreateModal} setIsOpen={setIsOpenCreateModal} parentPaymentMethods={paymentMethods} parentCategories={categories} getTransactions={getTransactions} currentPage={currentPage}/>
        </div>
    )
}

export default TransactionMainContent