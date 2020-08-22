import React, { useState } from 'react';
import styled from 'styled-components';

import { RoundGalleryVideo } from './RoundGalleryVideo';
import {RoundGalleryImage } from './RoundGalleryImage';

const Gallery = styled.div`
  font-size: 1em;
  font-weight: bold;
  background-color: white;
  padding: 45px;
`;

const Navigation = styled.div`
  display: flex;
  justify-content: center;
  align-items: baseline;
  margin-top: 20px;
  user-select: none;
`;

const NavNumbers = styled.span`
  margin: 0px 10px;
`;

interface NavNumberProps {
  selected: boolean;
}

const NavNumber = styled.span<NavNumberProps>`
  font-weight: ${(props) => props.selected && 'bolder'};
  color: ${(props) => props.selected && 'red'};
`;

const NavButton = styled.span`
  font-size: 1.5em;
  font-weight: bold;
  margin: 0px 5px;
`;

interface RoundGalleryProps {
  rounds: Array<{
    title: string;
    link: string;
  }>;
}

/**
 * Component for
 */
export const RoundGallery: React.FC<RoundGalleryProps> = ({ rounds }) => {
  const [selectedRound, setSelectedRound] = useState(0);

  const next = () => {
    if (selectedRound + 1 < rounds.length) {
      setSelectedRound((prev) => prev+1);
    }
  };

  const previous = () => {
    if (selectedRound - 1 >= 0) {
      setSelectedRound((prev) => prev-1);
    }
  }

  const first = () => {
    setSelectedRound(0);
  }

  const last = () => {
    setSelectedRound(rounds.length-1);
  }

  const url = new URL(rounds[selectedRound].link);
  const re = /^((www\.)?youtube\.com|youtu\.be)$/;

  return (
    <Gallery>
      {url.host.match(re) ? (
        <RoundGalleryVideo {...rounds[selectedRound]} />
      ) : (
        <RoundGalleryImage {...rounds[selectedRound]} />
      )}
      <Navigation>
        <NavButton onClick={first}>«</NavButton>
        <NavButton onClick={previous}>‹</NavButton>
        <NavNumbers>
          <NavNumber selected={selectedRound === 0}>1</NavNumber>
          ...
          {(selectedRound !== 0 && selectedRound !== rounds.length-1) && (<NavNumber selected={true}>{selectedRound+1}</NavNumber>)}
          {(selectedRound !== 0 && selectedRound !== rounds.length-1) && '...'}
          <NavNumber selected={selectedRound === rounds.length-1}>{rounds.length}</NavNumber>
        </NavNumbers>
        <NavButton onClick={next}>›</NavButton>
        <NavButton onClick={last}>»</NavButton>
      </Navigation>
    </Gallery>
  );
};
