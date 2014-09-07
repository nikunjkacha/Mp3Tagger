package com.uncompilable.mp3tagger.model;

/**
 * Enum containing all Genres as specified by the id3 standard.
 * @author dennis
 *
 */
public enum Genre {
	
	BLUES(0) {
		@Override
		public String toString() {
			return "Blues";
		}
	}, 
	CLASSIC_ROCK(1) {
		@Override
		public String toString() {
			return "Classic Rock";
		}
	},
	Country(2) {
		@Override
		public String toString() {
			return "Country";
		}
	},
	DANCE(3) {
		@Override
		public String toString() {
			return "Dance";
		}
	},
	DISCO(4) {
		@Override
		public String toString() {
			return "Disco";
		}
	},
	FUNK(5) {
		@Override
		public String toString() {
			return "Funk";
		}
	},
	GRUNGE(6) {
		@Override
		public String toString() {
			return "Grunge";
		}
	},
	HIP_HOP(7) {
		@Override
		public String toString() {
			return "Hip Hop";
		}
	},
	JAZZ(8) {
		@Override
		public String toString() {
			return "Jazz";
		}
	},
	METAL(9) {
		@Override
		public String toString() {
			return "Metal";
		}
	},
	NEW_AGE(10) {
		@Override
		public String toString() {
			return "New Age";
		}
	},
	OLDIES(11) {
		@Override
		public String toString() {
			return "Oldies";
		}
	},	
	OTHER(12) {
		@Override
		public String toString() {
			return "Other";
		}
	},
	POP(13) {
		@Override
		public String toString() {
			return "Pop";
		}
	},
	R_N_B(14) {
		@Override
		public String toString() {
			return "R&B";
		}
	},
	RAP(15) {
		@Override
		public String toString() {
			return "Rap";
		}
	},
	REGGAE(16) {
		@Override
		public String toString() {
			return "Reggae";
		}
	},
	ROCK(17) {
		@Override
		public String toString() {
			return "Rock";
		}
	},
	TECHNO(18) {
		@Override
		public String toString() {
			return "Techno";
		}
	},
	INDUSTRIAL(19) {
		@Override
		public String toString() {
			return "Industrial";
		}
	},
	ALTERNATIVE(20) {
		@Override
		public String toString() {
			return "Alternative";
		}
	},
	
	SKA(21) {
		@Override
		public String toString() {
			return "Ska";
		}
	},
	DEATH_METAL(22) {
		@Override
		public String toString() {
			return "Death Metal";
		}
	},
	PRANKS(23) {
		@Override
		public String toString() {
			return "Pranks";
		}
	},
	SOUNDTRACK(24) {
		@Override
		public String toString() {
			return "Soundtrack";
		}
	},
	EURO_TECHNO(25) {
		@Override
		public String toString() {
			return "Euro-Techno";
		}
	},
	AMBIENT(26) {
		@Override
		public String toString() {
			return "Ambient";
		}
	},
	TRIP_HOP(27) {
		@Override
		public String toString() {
			return "Trip Hop";
		}
	},
	VOCAL(28) {
		@Override
		public String toString() {
			return "Vocal";
		}
	},
	JAZZ_N_FUNK(29) {
		@Override
		public String toString() {
			return "Jazz & Funk";
		}
	},
	FUSION(30) {
		@Override
		public String toString() {
			return "Fusion";
		}
	},
	TRANCE(31) {
		@Override
		public String toString() {
			return "Trance";
		}
	},
	CLASSICAL(32) {
		@Override
		public String toString() {
			return "Classical";
		}
	},
	INSTRUMENTAL(33) {
		@Override
		public String toString() {
			return "Instrumental";
		}
	},
	ACID(34) {
		@Override
		public String toString() {
			return "Acid";
		}
	},
	HOUSE(35) {
		@Override
		public String toString() {
			return "House";
		}
	},
	GAME(36) {
		@Override
		public String toString() {
			return "Game";
		}
	},
	SOUND_CLIP(37) {
		@Override
		public String toString() {
			return "Sound Clip";
		}
	},
	GOSPEL(38) {
		@Override
		public String toString() {
			return "Gospel";
		}
	},
	NOISE(39) {
		@Override
		public String toString() {
			return "Noise";
		}
	},
	ALTERNATIVE_ROCK(40) {
		@Override
		public String toString() {
			return "Alternative Rock";
		}
	},
	BASS(41) {
		@Override
		public String toString() {
			return "Bass";
		}
	},
	SOUL(42) {
		@Override
		public String toString() {
			return "Soul";
		}
	},
	PUNK(43) {
		@Override
		public String toString() {
			return "Punk";
		}
	},
	SPACE(44) {
		@Override
		public String toString() {
			return "Space";
		}
	},
	MEDITATIVE(45) {
		@Override
		public String toString() {
			return "Meditative";
		}
	},
	INSTRUMENTAL_POP(46) {
		@Override
		public String toString() {
			return "Instrumental Pop";
		}
	},
	INSTRUMENTAL_ROCK(47) {
		@Override
		public String toString() {
			return "Instrumental Rock";
		}
	},
	ETHNIC(48) {
		@Override
		public String toString() {
			return "Ethnic";
		}
	},
	GOTHIC(49) {
		@Override
		public String toString() {
			return "Gothic";
		}
	},
	DARKWAVE(50) {
		@Override
		public String toString() {
			return "Darkwave";
		}
	},
	TECHNO_INDUSTRIAL(51) {
		@Override
		public String toString() {
			return "Techno-Industrial";
		}
	},
	ELECTRONIC(52) {
		@Override
		public String toString() {
			return "Electronic";
		}
	},
	POP_FOLK(53) {
		@Override
		public String toString() {
			return "Pop-Folk";
		}
	},
	EURODANCE(54) {
		@Override
		public String toString() {
			return "Eurodance";
		}
	},
	DREAM(55) {
		@Override
		public String toString() {
			return "Dream";
		}
	},
	SOUTHERN_ROCK(56) {
		@Override
		public String toString() {
			return "Southern Rock";
		}
	},
	COMEDY(57) {
		@Override
		public String toString() {
			return "Comedy";
		}
	},
	CULT(58) {
		@Override
		public String toString() {
			return "Cult";
		}
	},
	GANGSTA(59) {
		@Override
		public String toString() {
			return "Gangsta";
		}
	},
	TOP_40(60) {
		@Override
		public String toString() {
			return "Top 40";
		}
	},
	CHRISTIAN_RAP(61) {
		@Override
		public String toString() {
			return "Christian Rap";
		}
	},
	POP_FUNK(62) {
		@Override
		public String toString() {
			return "Pop/Funk";
		}
	},
	JUNGLE(63) {
		@Override
		public String toString() {
			return "Jungle";
		}
	},
	NATIVE_AMERICAN(64) {
		@Override
		public String toString() {
			return "Native American";
		}
	},
	CABARET(65) {
		@Override
		public String toString() {
			return "Cabaret";
		}
	},
	NEW_WAVE(66) {
		@Override
		public String toString() {
			return "New Wave";
		}
	},
	PSYCHEDELIC(67) {
		@Override
		public String toString() {
			return "Psychedelic";
		}
	},
	RAVE(68) {
		@Override
		public String toString() {
			return "Rave";
		}
	},
	SHOWTUNES(69) {
		@Override
		public String toString() {
			return "Showtunes";
		}
	},
	TRAILER(70) {
		@Override
		public String toString() {
			return "Trailer";
		}
	},
	LO_FI(71) {
		@Override
		public String toString() {
			return "Lo-Fi";
		}
	},
	TRIBAL(72) {
		@Override
		public String toString() {
			return "Tribal";
		}
	},
	ACID_PUNK(73) {
		@Override
		public String toString() {
			return "Acid Punk";
		}
	},
	ACID_JAZZ(74) {
		@Override
		public String toString() {
			return "Acid Jazz";
		}
	},
	POLKA(75) {
		@Override
		public String toString() {
			return "Polka";
		}
	},
	RETRO(76) {
		@Override
		public String toString() {
			return "Retro";
		}
	},
	MUSICAL(77) {
		@Override
		public String toString() {
			return "Musical";
		}
	},
	ROCK_N_ROLL(78) {
		@Override
		public String toString() {
			return "Rock & Roll1";
		}
	},
	HARD_ROCK(79) {
		@Override
		public String toString() {
			return "Hard Rock";
		}
	};
	
	
	private int value;
	
	private Genre(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
}
