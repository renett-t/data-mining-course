from typing import Tuple, List

import numpy as np


def calculate_hits(m: np.ndarray, iterations: int) -> Tuple[np.ndarray]:
    h = np.ones(shape=(m.shape[1], 1))
    a = None

    m_t = np.transpose(m)

    for i in range(iterations):
        if i > 0:
            h = np.matmul(m, a)
            mx = np.max(h)
            print(f'h, i={i + 1}, max={mx}')
            h /= mx

        if i < iterations - 1:
            a = np.matmul(m_t, h)
            mx = np.max(a)
            print(f'a, i={i+1}, max={mx}')
            a /= mx

    return (h, a)


if __name__ == "__main__":
    matrix = np.array(
        [
            [0, 1, 0, 0, 0, 0, 0],
            [0, 0, 1, 1, 1, 0, 0],
            [0, 0, 0, 0, 1, 0, 0],
            [0, 0, 0, 0, 1, 0, 0],
            [0, 0, 0, 0, 0, 1, 0],
            [0, 0, 0, 0, 0, 0, 0],
            [0, 0, 0, 1, 0, 0, 0]
        ]
    )

    hub, auth = calculate_hits(matrix, 4)

    print(f"Hubbiness:\n {hub}")
    print(f"Authority:\n {auth}")


