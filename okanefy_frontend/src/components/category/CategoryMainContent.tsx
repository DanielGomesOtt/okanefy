import { Button, Card, CardBody, CardFooter, CardHeader, Divider, Form, getKeyValue, Input, Modal, ModalBody, ModalContent, Pagination, Select, SelectItem, Table, TableBody, TableCell, TableColumn, TableHeader, TableRow } from '@heroui/react'
import { useEffect, useState } from 'react';
import { FiArrowUp, FiArrowDown, FiX } from "react-icons/fi"
import { FiSearch, FiPlus, FiEdit2, FiTrash2 } from "react-icons/fi";
import { BASE_URL } from '../../utils/constants';

function CategoryMainContent() {

    const [name, setName] = useState<string | null>(null)
    const [type, setType] = useState<string | null>(null)
    const [categoriesVisible, setCategoriesVisible] = useState(false)
    const [totalPages, setTotalPages] = useState(1)
    const [currentPage, setCurrentPage] = useState(1)
    const [categories, setCategories] = useState<Category[]>([])
    const [isOpenModal, setIsOpenModal] = useState(false)
    const [deletedCategoryId, setDeletedCategoryId] = useState<number | null>(null)
    const [editedCategoryId, setEditedCategoryId] = useState<number | null>(null)
    const [errorMessage, setErrorMessage] = useState("")

    const types = [
        {key: 1, name: 'INCOME'},
        {key: 2, name: 'EXPENSE'}
    ]

    interface Category {
        id: number
        name: string
        type: string
    }

    async function getCategories(page: number = 1) {
        try {
            const params = new URLSearchParams({
                page: String(page - 1),
                size: '25'
            })

            if (name) params.append('name', name)
            if (type) params.append('type', type)

            const response = await fetch(`${BASE_URL}category/${localStorage.getItem('id')}?${params}`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${localStorage.getItem('token')}`
                }
            })

            if (response.status !== 200) {
                const errorData = await response.json()
                throw new Error(errorData.message || "Erro ao tentar confirmar o e-mail.")
            }

            const data = await response.json()
            setTotalPages(data.totalPages)
            setCurrentPage(data.number + 1)
            if(data.categories.length > 0) {""
                setCategoriesVisible(true)
                setCategories(data.categories)
            } else {
                setCategories([])
                setCategoriesVisible(false)
            }

        } catch (error: unknown) {
            setCategories([])
            setCategoriesVisible(false)
            console.log(error)    
        }
    }

    async function deleteCategory() {
        try {
            
            const response = await fetch(`${BASE_URL}category/${deletedCategoryId}`, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${localStorage.getItem('token')}`
                }
            })

            if (response.status !== 204) {
                const errorData = await response.json()
                throw new Error(errorData.message || "Erro ao tentar excluir categoria.")
            }

            setIsOpenModal(false)
            getCategories(1)

        } catch (error: unknown) {
            console.log(error)    
        }
    }

    async function saveCategory() {
        try {
            const body = {
                "name": name,
                "type": type,
                "user_id": localStorage.getItem("id")
            }

            const response = await fetch(`${BASE_URL}category`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${localStorage.getItem('token')}`
                },
                body: JSON.stringify(body)
            })
            
            if(response.status !== 201) {
                const errorData = await response.json()
                throw new Error(errorData.message || "Erro ao tentar cadastrar uma nova categoria.")
            }
            
            getCategories(currentPage)
        } catch (error: unknown) {
            if (error instanceof Error) {
                setErrorMessage(error.message)
                setTimeout(() => {
                    setErrorMessage("")
                }, 5000)
            } else {
                console.log(error)
            }
        }
    }

    async function updateCategory() {
        try {
            const body = {
                "id": editedCategoryId,
                "name": name,
                "type": type,
            }

            const response = await fetch(`${BASE_URL}category`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${localStorage.getItem('token')}`
                },
                body: JSON.stringify(body)
            })
            
            if(response.status !== 200) {
                const errorData = await response.json()
                throw new Error(errorData.message || "Erro ao tentar editar uma categoria.")
            }
            setEditedCategoryId(null)
            setName("")
            setType("")
            getCategories(currentPage)
        } catch (error: unknown) {
            if (error instanceof Error) {
                setErrorMessage(error.message)
                setTimeout(() => {
                    setErrorMessage("")
                }, 5000)
            } else {
                console.log(error)
            }
        }
    }

    useEffect(() => {
        getCategories()
    }, []);

    return (
        <div className='w-4/5 h-4/5'>
            <Card className='w-full h-full'>
                <CardHeader>
                    <Form className="w-full" onSubmit={(e) => e.preventDefault()}>
                        <div className='text-center w-full'>
                            <span className='text-center text-red-500'>{errorMessage}</span>
                        </div>
                        <div className="flex flex-wrap md:flex-nowrap items-center gap-4 w-full justify-center">
                            <Input
                                className="w-full md:w-96"
                                name="category_input"
                                placeholder="Insira o nome de uma categoria."
                                type="text"
                                onChange={(e) => setName(e.target.value)}
                                value={name !== null ? name : ''}
                            />

                            <Select
                                label="Selecione um tipo"
                                className="w-full md:max-w-xs"
                                selectedKeys={[type ?? "all"]}
                                onSelectionChange={(keys) => {
                                    const selected = String(Array.from(keys)[0]);
                                    setType(selected === "all" ? null : selected);
                                }}
                            >
                                <SelectItem key="all">Todos</SelectItem>

                                <>
                                    {types.map((t) => (
                                        <SelectItem key={t.name}>
                                        {t.name === "INCOME" ? "Renda" : "Gasto"}
                                        </SelectItem>
                                    ))}
                                </>
                            </Select>

                            <div className="flex w-full md:w-auto justify-between md:justify-start gap-2">
                            {editedCategoryId === null ? (
                                <>
                                    <Button
                                        className="bg-primary text-white flex-1 md:flex-none"
                                        startContent={<FiSearch size={18} />}
                                        onPress={() => getCategories(1)}
                                    >
                                        Pesquisar
                                    </Button>

                                    <Button
                                        className="bg-success text-white flex-1 md:flex-none"
                                        startContent={<FiPlus size={18} />}
                                        onPress={saveCategory}
                                    >
                                        Adicionar
                                    </Button>
                                </>
                            ) : (
                                <>
                                    <Button
                                        className="bg-success text-white flex-1 md:flex-none"
                                        startContent={<FiEdit2 size={18} />}
                                        onPress={updateCategory}
                                    >
                                        Editar
                                    </Button>

                                    <Button
                                        className="bg-danger text-white flex-1 md:flex-none"
                                        startContent={<FiX size={18} />}
                                        onPress={() => {
                                            setEditedCategoryId(null)
                                            setName("")
                                            setType("")
                                        }}
                                    >
                                        Cancelar
                                    </Button>
                                </>
                            )}
                            </div>
                        </div>    
                    </Form>
                </CardHeader>
                <Divider />
                <CardBody>
                    <Table aria-labelledby='Tabela de categorias'>
                        <TableHeader>
                            <TableColumn key="id" className='text-bold text-blue-500 text-center'>ID</TableColumn>
                            <TableColumn key="name" className='text-bold text-blue-500 text-center'>Nome</TableColumn>
                            <TableColumn key="type" className='text-bold text-blue-500 text-center'>Tipo</TableColumn>
                            <TableColumn key="edit" className='text-bold text-blue-500 text-center'>Editar</TableColumn>
                            <TableColumn key="delete" className='text-bold text-blue-500 text-center'>Excluir</TableColumn>
                        </TableHeader>

                        <TableBody items={categories}>
                            {(category) => (
                                <TableRow key={category.id}>
                                {(columnKey) => (
                                    <TableCell className="text-center">
                                    {columnKey === "edit" ? (
                                        <Button
                                            isIconOnly
                                            size="sm"
                                            color="primary"
                                            variant="bordered"
                                            className="transition-all duration-200 hover:bg-primary hover:text-white"
                                            onPress={() => {
                                                setEditedCategoryId(category.id)
                                                setName(category.name)
                                                setType(category.type)
                                            }}
                                        >
                                            <FiEdit2 className="w-4 h-4" />
                                        </Button>
                                    ) : columnKey === "delete" ? (
                                        <Button
                                            isIconOnly
                                            size="sm"
                                            color="danger"
                                            variant="bordered"
                                            className="transition-all duration-200 hover:bg-danger hover:variant-solid hover:text-white"
                                            onPress={() => {
                                                setIsOpenModal(true)
                                                setDeletedCategoryId(category.id)
                                            }}
                                        >
                                            <FiTrash2 className="w-4 h-4" />
                                        </Button>
                                    ) : columnKey === "type" ? (
                                        category.type === "INCOME" ? (
                                        <div className="flex items-center justify-center gap-1 text-green-500 font-semibold">
                                            <FiArrowUp className="w-4 h-4" />
                                            <span>RENDA</span>
                                        </div>
                                        ) : (
                                        <div className="flex items-center justify-center gap-1 text-red-500 font-semibold">
                                            <FiArrowDown className="w-4 h-4" />
                                            <span>GASTO</span>
                                        </div>
                                        )
                                    ) : (
                                        getKeyValue(category, columnKey)
                                    )}
                                    </TableCell>
                                )}
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
                        getCategories(page);
                    }}/>
                </CardFooter>
            </Card>
            <Modal isOpen={isOpenModal} onOpenChange={setIsOpenModal} hideCloseButton>
                <ModalContent>
                    {() => (
                        <>
                            <ModalBody className="text-center">
                            <h3 className="text-xl font-bold">
                                Você tem certeza que deseja excluir essa categoria?
                            </h3>

                            <div className="flex justify-around">
                                <Button className="bg-green-500 text-white p-5" onPress={deleteCategory}>Confirmar</Button>
                                <Button className="bg-red-500 text-white p-5" onPress={() => setIsOpenModal(false)}>Cancelar</Button>
                            </div>
                            </ModalBody>
                        </>
                    )}
                </ModalContent>
            </Modal>

        </div>
    )
}

export default CategoryMainContent