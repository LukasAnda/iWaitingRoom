import { Pacient } from '../../app/pacient.model';

export const pacienti: Pacient[] = [
    {
        meno: "Robert Druska",
        email: "robert.druska@gmail.com",
        id: 123,
        poistovna: "VSEOBECNA",
        krvnaSkupina: "A",
        krvRH: "382747436",
        alergie: [
            'kvety',
            'svetlo'
        ],
        ochorenia: [
            'chripka',
            'huntington'
        ],
        operacie: [],
        lieky: [
            'a',
            'b',
            'c'
        ]
    },
    {
        meno: "Martin Bohdal",
        email: "martin.bohdal@gmail.com",
        id: 7777,
        poistovna: "VSEOBECNA",
        krvnaSkupina: "0",
        krvRH: "3724728274",
        alergie: [
            'muka',
            'arasidy'
        ],
        ochorenia: [
            'aids',
            'ruka zlomena'
        ],
        operacie: [
            "operacia kolena",
            "sedy zakal"
        ],
        lieky: [
            'a',
            'b',
            'c'
        ]
    },
    {
        meno: "Richard Sokol",
        email: "riso.soky@gmail.com",
        id: 4321,
        poistovna: "SUKROMNA",
        krvnaSkupina: "AB",
        krvRH: "1092109390",
        alergie: [
            'pupava',
            'cukor'
        ],
        ochorenia: [
            'lepra',
            'malaria'
        ],
        operacie: [],
        lieky: [
            'aaaaaa',
            'bbbbbbbbb',
            'cccccccc'
        ]
    }
]

export const pacient: Pacient = {
    meno: "Robert Druska",
    email: "robert.druska@gmail.com",
    id: 123,
    poistovna: "VSEOBECNA",
    krvnaSkupina: "A",
    krvRH: "382747436",
    alergie: [
        'kvety',
        'svetlo'
    ],
    ochorenia: [
        'chripka',
        'huntington'
    ],
    operacie: [],
    lieky: [
        'a',
        'b',
        'c'
    ]
}

