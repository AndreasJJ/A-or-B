import React, { useEffect } from 'react';
import styled from 'styled-components';

interface WrapperProps {
  type?: "info" | "success" | "error";
  styling?: string;
}

const Wrapper = styled.div<WrapperProps>`
    position: relative;
    background-color: ${(props) => {
      switch(props.type) {
        case "info": {
          return "#1368CE";
        }
        case "success": {
          return "#26890C";
        }
        case "error": {
          return "#E21B3C";
        }
        default: {
          return "#26890C";
        }
      }
    }};
    padding: .5rem 1rem;
    border-radius: .25rem;
    color: #ffffff;
    font-weight: 700;
    text-decoration: none;
    display: inline-block;
    top: -.0625rem;
    text-align: center;
    user-select: none;

    ${(props) => props.styling}

    &::before {
      content: ' ';
      position: absolute;
      left: 0;
      width: 100%;
      top: 0;
      bottom: -.1875em;
      background-color: inherit;
      z-index: -1;
      border-radius: .25em;
    }

    &::after {
      content: ' ';
      position: absolute;
      left: 0;
      width: 100%;
      top: 0;
      bottom: -.1875em;
      background: rgba(0,0,0,0.25);
      z-index: -1;
      border-radius: .25em;
    }

    &:hover {
      text-decoration: none;
      top: 0em;
      cursor: pointer;
    }

    &:hover:before, &:hover:after {
      bottom: -.125em;
    }
`;

interface ButtonProps {
  text: string;
  type?: "info" | "success" | "error";
  onClick?: (e: React.MouseEvent<HTMLDivElement, MouseEvent>) => void;
  styling?: string;
}

/**
 * Button
 */
export const Button: React.FC<ButtonProps> = (props) => {
  return (
    <Wrapper onClick={props.onClick} type={props.type} styling={props.styling}>
        {props.text}
    </Wrapper>
  );
};