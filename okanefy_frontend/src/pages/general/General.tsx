import React from 'react'
import GeneralNavBar from '../../components/general/GeneralNavBar'
import GeneralMainContent from '../../components/general/GeneralMainContent'

function General() {
  return (
    <div className='w-screen h-screen'>
        <div className='h-screen flex flex-col w-full'>
          <GeneralNavBar />
          <div className="flex-1 flex flex-col mt-24 items-center gap-4">
            <GeneralMainContent />
          </div>
        </div>
    </div>
  )
}

export default General