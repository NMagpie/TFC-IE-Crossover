import json
from glob import glob

from mcresources import utils

assets_path = '../src/main/resources/assets/'
texture_forgiveness_paths = ('mallet', 'metal/full',)


def main():
    model_locations = glob(assets_path + 'tfc_ie_addon/models/**/*.json', recursive=True)
    state_locations = glob(assets_path + 'tfc_ie_addon/blockstates/**/*.json', recursive=True)
    validate_model_parents(model_locations)
    validate_textures(model_locations)
    validate_blockstate_models(state_locations)


def validate_blockstate_models(state_locations):
    tested = 0
    errors = 0
    for f in state_locations:
        state_file = load(f)
        if 'variants' in state_file:
            variants = state_file['variants']
            for variant in variants.values():
                if 'model' in variant:
                    model = variant['model']
                    tested, errors = find_model_file(f, model, tested, errors, 'Blockstate file %s points to non-existent model: %s')
        elif 'multipart' in state_file:
            multipart = state_file['multipart']
            for mp in multipart:
                if 'apply' in mp:
                    apply = mp['apply']
                    model = None
                    if isinstance(apply, list):
                        for entry in apply:
                            if 'model' in entry:
                                model = entry['model']
                    elif 'model' in apply:
                        model = apply['model']
                    if model is not None:
                        tested, errors = find_model_file(f, model, tested, errors, 'Blockstate file %s points to non-existent model: %s')
    print('Blockstate Validation: Validated %s files, found %s errors' % (tested, errors))


def validate_model_parents(model_locations):
    tested = 0
    errors = 0
    for f in model_locations:
        model_file = load(f)
        if 'parent' in model_file:
            parent = model_file['parent']
            tested, errors = find_model_file(f, parent, tested, errors, 'Model parent not found. Model: %s, Parent: %s')
    print('Parent Validation: Validated %s files, found %s errors' % (tested, errors))


def validate_textures(model_locations):
    tested = 0
    files_tested = 0
    errors = 0
    existing_textures = []
    for f in model_locations:
        model_file = load(f)
        if 'textures' in model_file:
            textures = model_file['textures']
            if isinstance(textures, dict):
                files_tested += 1
                for texture in textures.values():
                    if '#' not in texture:
                        res = utils.resource_location(texture)
                        if res.domain == 'tfc_ie_addon':
                            tested += 1
                            path = assets_path + 'tfc_ie_addon/textures/%s.png' % res.path
                            if len(glob(path)) == 0:
                                print('Using missing texture, unable to load %s : java.io.FileNotFoundException: %s' % (f, path))
                                errors += 1
                            else:
                                existing_textures.append(path)
    for f in glob(assets_path + 'tfc_ie_addon/textures/**/*.png', recursive=True):
        f = f.replace('\\', '/')
        if f not in existing_textures and ('block/' in f or 'item/' in f):
            forgiven = False
            for check in texture_forgiveness_paths:
                if check in f:
                    forgiven = True
            if not forgiven:
                print('Texture not matched to any model file: %s' % f)
                errors += 1

    print('Texture Validation: Verified %s files, %s texture entries, found %s errors' % (files_tested, tested, errors))


def find_model_file(file_path: str, initial_path: str, tested: int, errors: int, on_error: str):
    res = utils.resource_location(initial_path)
    if res.domain == 'tfc_ie_addon':
        tested += 1
        path = assets_path + 'tfc_ie_addon/models/%s.json' % res.path
        found = len(glob(path))
        if found != 1:
            print(on_error % (file_path, path))
            errors += 1
    return tested, errors


def load(fn: str):
    with open(fn, 'r', encoding='utf-8') as f:
        return json.load(f)


if __name__ == '__main__':
    main()
