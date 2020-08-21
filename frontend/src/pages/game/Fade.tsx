import React, { useEffect, useState } from 'react';
import styled from 'styled-components';

interface WrapperProps {
  color: string;
}

const Wrapper = styled.div<WrapperProps>`
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    height: 100%;
    width: 100%;
    font-size: xxx-large;
    background-color: ${(props) => props.color};

    transition: background-color 1s linear;
`;

interface PinProps {
  children?: React.ReactNode
}

/**
 * Pin component
 */
export const Fade: React.FC<PinProps> = (props) => {
  const colors = ['#F7F6CF', '#B6D8F2', '#F4CFDF', '#5784BA', '#9AC8EB'];
  const [color, setColor] = useState(0);

  useEffect(() => {
    const interval = setInterval(() => { 
      setColor((prev) => (prev < (colors.length - 1) ? prev+1 : 0));
    }, 3000);

    return () => {
      clearInterval(interval);
    }
  }, []);

  return (
    <Wrapper color={colors[color]}>
        {props.children}
    </Wrapper>
  );
};

