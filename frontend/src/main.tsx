import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import MarkForm from './MarkForm.tsx'
import Header from './Header.tsx'

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <Header />
    <MarkForm />
  </StrictMode>,
)
