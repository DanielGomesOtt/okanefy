import { createRoot } from 'react-dom/client'
import './index.css'
import { BrowserRouter, Routes, Route } from 'react-router'
import { HeroUIProvider } from '@heroui/react';
import { StrictMode } from 'react';
import Home from './pages/home/Home';
import General from './pages/general/General';
import PublicRoute from './routes/PublicRoute';
import PrivateRoute from './routes/PrivateRoute';

const rootElement = document.getElementById('root');

if (rootElement) {
  createRoot(rootElement).render(
    <StrictMode>
        <HeroUIProvider>
            <BrowserRouter>
                <Routes>
                    <Route path='/' element={<PublicRoute><Home /></PublicRoute>}/>
                    <Route path='/geral' element={<PrivateRoute><General /></PrivateRoute>}/>
                </Routes>
            </BrowserRouter>
        </HeroUIProvider>
    </StrictMode>
  );
} else {
  console.error('Root element not found');
}
