# Test Coverage Log

## Iteration 1

**Date:** March 8, 2026

### Targets Selected
- **Model/price/Price** class (Priority: HIGH)
  - Initial coverage: 6.98% (3/43 lines covered)
  - 40 lines uncovered across 12 methods

### Tests Added/Modified
- **Created:** `src/test/java/Model/price/PriceTest.java`
  - 43 comprehensive test methods covering:
    - `isNegative()` - 3 tests (negative, positive, zero cases)
    - `greaterThan()` - 4 tests (larger, smaller, equal, null)
    - `lessThan()` - 4 tests (smaller, larger, equal, null)
    - `greaterOrEqual()` - 4 tests (larger, equal, smaller, null)
    - `lessOrEqual()` - 4 tests (smaller, equal, larger, null)
    - `add()` - 3 tests (positive, negative, null)
    - `subtract()` - 3 tests (positive, negative result, null)
    - `multiply()` - 3 tests (positive, zero, negative multiplier)
    - `equals()` - 4 tests (same, different, null, different class)
    - `hashCode()` - 2 tests (consistency, different values)
    - `compareTo()` - 4 tests (smaller, larger, equal, null)
    - `toString()` - 5 tests (positive, negative, zero, large, small)

### Coverage Improvement
- **Price class:** 6.98% → **100%** (+93.02%)
- **Lines covered:** 3 → 43 (+40 lines)
- **All 12 methods:** Fully covered with normal, edge, and error cases

### Codebase Bugs Found/Fixed
None - all tests passed on first run. The Price class implementation is robust.

### Test Results
- Total tests: 46 (43 new + 3 existing)
- ✅ All tests passed
- Build status: SUCCESS

---

## Iteration 2

**Date:** March 8, 2026

### Targets Selected
- **Service/EncryptionService** (Priority: HIGH - Security Critical)
  - Initial coverage: 0% (0/3 lines covered)
  - Critical password hashing and verification functionality
- **Model/user/Wallet** (Priority: HIGH - Business Logic)
  - Initial coverage: 0% (0/16 lines covered)
  - Money management with validation logic

### Tests Added/Modified
- **Created:** `src/test/java/Service/EncryptionServiceTest.java`
  - 14 comprehensive test methods covering:
    - `hashPassword()` - 7 tests (basic, empty, special chars, long strings, uniqueness)
    - `verifyPassword()` - 6 tests (correct, incorrect, case-sensitive, special chars, whitespace)
    - Integration test - multiple password scenarios
    - Constructor instantiation test

- **Created:** `src/test/java/Model/user/WalletTest.java`
  - 17 comprehensive test methods covering:
    - Constructor - 2 tests (valid price, zero price)
    - `getWalletValue()` - 1 test
    - `addMoney()` - 5 tests (positive, multiple adds, zero/negative validation)
    - `subtractMoney()` - 7 tests (valid, multiple subtracts, exact amount, zero/negative validation, insufficient funds)
    - Integration tests - 2 tests (add/subtract combos, large amounts)

### Coverage Improvement
- **EncryptionService:** 0% → **100%** (+100%)
  - Lines covered: 0 → 3 (+3 lines)
  - All 2 static methods + constructor fully covered
- **Wallet:** 0% → **100%** (+100%)
  - Lines covered: 0 → 16 (+16 lines)
  - All 4 methods fully covered with validation and edge cases

### Codebase Bugs Found/Fixed
None - all tests passed. Both classes have robust error handling and validation.

### Test Results
- Total tests: 74 (43 Price + 17 Wallet + 14 EncryptionService)
- ✅ All tests passed
- Build status: SUCCESS

---
