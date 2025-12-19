import { Card, CardHeader } from '@heroui/react'


function GeneralMainContent() {
	return (
		<div className='w-4/5 h-4/5'>
			<div className='w-full h-full grid grid-cols-1 md:grid-cols-3 gap-4'>
				<Card className='w-full h-36 bg-blue-400'>
					<CardHeader className='text-center'>
						<div className='w-full text-center'>
							<span className='text-white text-semibold'>Movimentação</span>
						</div>
					</CardHeader>
				</Card>

				<Card className='w-full h-36 bg-green-400'>
					<CardHeader>
						<div className='w-full text-center'>
							<span className='text-white text-semibold'>Entrada</span>
						</div>
					</CardHeader>
				</Card>

				<Card className='w-full h-36 bg-red-400'>
					<CardHeader>
						<div className='w-full text-center'>
							<span className='text-white text-semibold'>Saída</span>
						</div>
					</CardHeader>
				</Card>
			</div>
		</div>
	)
}

export default GeneralMainContent