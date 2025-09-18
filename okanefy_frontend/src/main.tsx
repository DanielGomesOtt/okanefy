import { createRoot } from 'react-dom/client'
import './index.css'
import { BrowserRouter, Routes, Route } from 'react-router'
import { HeroUIProvider } from '@heroui/react';
import { StrictMode } from 'react';
import Home from './pages/home/Home';

const rootElement = document.getElementById('root');

if (rootElement) {
  createRoot(rootElement).render(
    <StrictMode>
        <HeroUIProvider>
            <BrowserRouter>
                <Routes>
                    <Route path='/' element={<Home />}/>
                </Routes>
            </BrowserRouter>
        </HeroUIProvider>
    </StrictMode>
  );
} else {
  console.error('Root element not found');
}
