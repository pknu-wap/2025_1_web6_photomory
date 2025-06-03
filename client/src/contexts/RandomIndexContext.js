import { createContext, useState, useContext } from 'react';

const RandomIndexContext = createContext();

export function RandomIndexProvider({ children }) {
  const [randomIndex, setRandomIndex] = useState(null);

  const updateRandomIndex = (index) => {
    setRandomIndex(index);
  };

  return (
    <RandomIndexContext.Provider value={{ randomIndex, updateRandomIndex }}>
      {children}
    </RandomIndexContext.Provider>
  );
}

export function useRandomIndex() {
  const context = useContext(RandomIndexContext);
  if (!context) {
    throw new Error('useRandomIndex must be used within a RandomIndexProvider');
  }
  return context;
} 