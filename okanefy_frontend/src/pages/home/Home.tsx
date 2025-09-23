import HomeNavbar from '../../components/home/HomeNavbar'
import Cards from './Cards'

function Home() {
  return (
    <div>
        <HomeNavbar />
        <div className="w-screen grid grid-cols-1 md:grid-cols-2 lg:gap-48 px-2 md:px-12 md:py-10 items-center">
          <div className=" text-left xl:pl-54">
              <h2 className='text-2xl md:text-5xl fira-sans-bold text-slate-800'>O equilíbrio de um samurai para sua vida financeira</h2>
              <p className='text-xl md:text-2xl text-slate-600 mt-5'>Controle seus gastos de maneira simples e eficaz</p>
              <div className='block lg:flex justify-between mt-12 gap-4'>
                <button className='bg-blue-500 px-18 py-3 text-slate-100 hover:text-white rounded-2xl mb-3 lg:mb-0 w-full hover:cursor-pointer hover:shadow-2xl'>Login</button>
                <button className='bg-green-500 px-18 py-3 text-slate-100 hover:text-white rounded-2xl mt-3 lg:mt-0 w-full hover:cursor-pointer hover:shadow-2xl'>Registrar</button>
              </div>
          </div>
          <div className='hidden md:flex xl:pr-48'>
              <img src="src\assets\ilustration-home.png" alt="samurai" className='w-lg lg:w-xl'/>
          </div>
        </div>
    </div>
  )
}

export default Home