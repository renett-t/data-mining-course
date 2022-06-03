import numpy as np
import json


def isDifferent(old_m, new_m):
    epsilon = 0.001

    for k in range(old_m.size):
        if abs(old_m[k] - new_m[k]) > epsilon:
            return True

    return False


if __name__ == '__main__':
    links_json_file = "../result/result.json"
    print("START")

    with open(links_json_file, "r") as f:
        links_dictionary: dict = json.load(f)
    links_map = {d["parent"]: d["children"] for d in links_dictionary}

    all_links = []
    for parent, children in links_map.items():
        all_links.append(parent)
        all_links += children

    for link in all_links:
        if link not in links_map.keys():
            connected_links = []
            for parent, children in links_map.items():
                if link in children:
                    connected_links.append(parent)
            print(f"new link! {len(connected_links)}-- {link}")
            if len(connected_links) > 5:
                links_map[link] = connected_links

    unique_links = list(links_map.keys())
    unique_links_num = len(unique_links)

    print("CALCULATING MATRIX")

    matrix = np.zeros(shape=(unique_links_num, unique_links_num))

    print(f"matrix len {unique_links_num}x{unique_links_num}")
    for i in range(unique_links_num):
        for j in range(unique_links_num):
            if unique_links[i] in links_map[unique_links[j]]:
                matrix[i][j] = 1.0/len(links_map[unique_links[j]])
                print(f"[{i}][{j}]: {matrix[i][j]}")

    v0 = np.full((unique_links_num,), 1.0 / unique_links_num)
    print(v0)

    weights = np.matmul(matrix, v0)

    oldMatrix = weights
    newMatrix = np.matmul(matrix, weights)
    count = 1
    while isDifferent(oldMatrix, newMatrix):
        print("calculation iteration")
        weights = newMatrix
        count += 1
        if count > 100000:
            break

        oldMatrix = weights
        newMatrix = np.matmul(matrix, weights)

    print(f"iterations = {count}")
    weights_map = {}
    for links_name, weight in zip(unique_links, weights):
        weights_map[links_name] = weight

    print("writing to ranks.json")
    with open("ranks.json", "w") as f:
        json.dump(weights_map, f)

    sorted_weight_map = sorted(weights_map.items(), key=lambda x: x[1], reverse=True)
    print(sorted_weight_map)

    print("writing to links_sorted.txt")
    with open("links_sorted.txt", "w") as f:
        for items in sorted_weight_map:
            print(f"{items[0]}: {items[1]}", file=f)
