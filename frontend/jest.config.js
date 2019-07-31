module.exports = {
    roots: ['<rootDir>/src'],
    transform: {
      '^.+\\.tsx?$': 'ts-jest',
      "^.+\\.js$": "babel-jest",
    },
    testRegex: '(src/__tests__/.*\\.(test|spec))\\.tsx?$',
    moduleFileExtensions: ['ts', 'tsx', 'js', 'jsx', 'json', 'node'],
    moduleNameMapper: {
        "\\.(css|less|scss)$": "identity-obj-proxy"
    },
    setupFiles: ["./src/setup-test.js"],
}