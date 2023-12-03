import React from 'react';
import logo from './logo.svg';
import './App.css';
import { BrowserRouter, Route, Router, Routes } from 'react-router-dom';
import CodeBlock from './components/CodeBlock/CodeBlock';
import Lobby from './components/Lobby/Lobby';

function App() {
  return (
    <BrowserRouter>
        <Routes>
          <Route path="/" element={<Lobby />} />
          <Route path="/codeblock" element={<CodeBlock />} />
        </Routes>
    </BrowserRouter>
  );
}

export default App;
