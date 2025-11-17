import GeneralNavBar from '../../components/general/GeneralNavBar'
import CategoryMainContent from '../../components/category/CategoryMainContent'

function Category() {
  return (
    <div className='w-screen h-screen'>
        <div className='h-screen flex flex-col w-full'>
          <GeneralNavBar />
          <div className="flex-1 flex flex-col mt-24 items-center gap-4">
            <CategoryMainContent />
          </div>
        </div>
    </div>
  )
}

export default Category