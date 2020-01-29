-- phpMyAdmin SQL Dump
-- version 4.9.3
-- https://www.phpmyadmin.net/
--
-- Host: localhost:8889
-- Generation Time: Jan 27, 2020 at 03:06 PM
-- Server version: 5.7.26
-- PHP Version: 7.4.1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";

--
-- Database: `greenGuyDataBase`
--

-- --------------------------------------------------------

--
-- Table structure for table `adressOpinion`
--

CREATE TABLE `adressOpinion` (
  `adress` varchar(150) NOT NULL,
  `id` int(11) NOT NULL,
  `opinion` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `commentary`
--

CREATE TABLE `commentary` (
  `sourceId` int(11) NOT NULL,
  `receiverId` int(11) NOT NULL,
  `opinion` text NOT NULL,
  `date` date NOT NULL,
  `time` time NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='commentaires';

-- --------------------------------------------------------

--
-- Table structure for table `contactList`
--

CREATE TABLE `contactList` (
  `id1` int(11) NOT NULL,
  `id2` int(11) NOT NULL,
  `date` date NOT NULL,
  `time` time NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='liste de contact';

-- --------------------------------------------------------

--
-- Table structure for table `creationOfEvents`
--

CREATE TABLE `creationOfEvents` (
  `name` varchar(300) NOT NULL,
  `time` time NOT NULL,
  `date` date NOT NULL,
  `localisation` point NOT NULL,
  `creatorId` int(11) NOT NULL,
  `eventId` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='création des événements';

-- --------------------------------------------------------

--
-- Table structure for table `ecoFriendlyAdress`
--

CREATE TABLE `ecoFriendlyAdress` (
  `adress` varchar(150) NOT NULL,
  `localisation` point NOT NULL,
  `carbonFootprint` int(11) NOT NULL COMMENT 'in gCO2eq/kWh',
  `description` text NOT NULL,
  `creatorId` int(11) NOT NULL,
  `opinion` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='adresse eco-friendly';

-- --------------------------------------------------------

--
-- Table structure for table `interestedPeople`
--

CREATE TABLE `interestedPeople` (
  `eventId` int(11) NOT NULL,
  `interestedPeopleId` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `listOfGroups`
--

CREATE TABLE `listOfGroups` (
  `name` varchar(150) NOT NULL,
  `groupId` int(11) NOT NULL,
  `creatorId` int(11) NOT NULL,
  `date` date NOT NULL,
  `time` time NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='recensements des groupes';

-- --------------------------------------------------------

--
-- Table structure for table `memberOfGroups`
--

CREATE TABLE `memberOfGroups` (
  `id` int(11) NOT NULL,
  `id_group` int(11) NOT NULL,
  `joiningDate` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='qui est dans quel groupe';

-- --------------------------------------------------------

--
-- Table structure for table `messagesBetweenUsers`
--

CREATE TABLE `messagesBetweenUsers` (
  `senderId` int(11) NOT NULL,
  `receiverId` int(11) NOT NULL,
  `message` text NOT NULL,
  `date` date NOT NULL,
  `time` time NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='messages d''utilisateurs à utilisateurs ';
