import React, { useRef } from 'react';

function useFocus(): [React.MutableRefObject<HTMLInputElement | null>, () => void]  {
  const htmlElRef = useRef<HTMLInputElement>(null);
  const setFocus = () => {
    if (htmlElRef.current) {
      htmlElRef.current.focus();
    }
  }

  return [ htmlElRef, setFocus ];
}

export {
  useFocus,
};