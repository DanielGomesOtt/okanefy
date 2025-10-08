import { Card, CardHeader, CardBody, Divider, Image } from '@heroui/react'

function Cards() {
  return (
    <div className='grid grid-cols-1 md:grid-cols-3 justify-items-center mt-5 px-2 md:px-12 lg:px-12 xl:px-64 gap-2'>
        <Card className="border-1 border-green-500 w-full">
            <CardHeader className="flex gap-3 justify-center bg-green-500">
                <div>
                    <Image
                        alt="controle financeiro"
                        height={40}
                        radius="sm"
                        src="src/assets/controle_financeiro.png"
                        width={40}
                    />
                </div>
                <div>
                <p className="text-md text-white">Controle Financeiro</p>
                </div>
            </CardHeader>
            <Divider className='bg-green-500'/>
            <CardBody>
                <p>Acompanhe todas as suas entradas e saídas em um só lugar.</p>
            </CardBody>
            </Card>
            <Card className="border-1 border-red-500 w-full">
            <CardHeader className="flex gap-3 justify-center bg-red-500">
                <div>
                    <Image
                        alt="adicionar despesas"
                        height={40}
                        radius="sm"
                        src="src/assets/adicionar_despesas.png"
                        width={40}
                    />
                </div>
                <div>
                <p className="text-md text-white">Adicionar Despesas</p>
                </div>
            </CardHeader>
            <Divider className='bg-red-500'/>
            <CardBody>
                <p>Cadastre suas despesas em poucos cliques.</p>
            </CardBody>
            </Card>
            <Card className="border-1 border-blue-500 w-full">
            <CardHeader className="flex gap-3 justify-center bg-blue-500">
                <div>
                    <Image
                        alt="relatórios inteligentes"
                        height={40}
                        radius="sm"
                        src="src/assets/relatorios_inteligentes.png"
                        width={40}
                    />
                </div>
                <div>
                <p className="text-md text-white">Relatórios Inteligentes</p>
                </div>
            </CardHeader>
            <Divider className='bg-blue-500'/>
            <CardBody>
                <p>Visualize gráficos e insights para entender seus gastos.</p>
            </CardBody>
        </Card>
    </div>
  )
}

export default Cards