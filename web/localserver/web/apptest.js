"use strict";
var describe;
var it;
var expect;
describe("Tests of basic math functions", function () {
    it("Adding 1 should work", function () {
        var foo = 0;
        foo += 1;
        expect(foo).toEqual(1);
    });
    it("Subtracting 1 should work", function () {
        var foo = 0;
        foo -= 1;
        expect(foo).toEqual(-1);
    });
});
